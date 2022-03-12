package de.debuglevel.responder.answer

import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnswerControllerTests {
    @Inject
    lateinit var answerClient: AnswerClient

    @ParameterizedTest
    @MethodSource("answerProvider")
    fun `add answer`(answer: Answer) {
        // Arrange
        val addAnswerRequest = AddAnswerRequest(answer)

        // Act
        val addedAnswer = answerClient.add(addAnswerRequest).block()

        // Assert
        Assertions.assertThat(addedAnswer.name).isEqualTo(answer.name)
        Assertions.assertThat(addedAnswer.name).isEqualTo(addAnswerRequest.name)
    }

    @ParameterizedTest
    @MethodSource("answerProvider")
    fun `get answer`(answer: Answer) {
        // Arrange
        val addAnswerRequest = AddAnswerRequest(answer)
        val addedAnswer = answerClient.add(addAnswerRequest).block()

        // Act
        val getAnswer = answerClient.get(addedAnswer.id).block()

        // Assert
        Assertions.assertThat(getAnswer.id).isEqualTo(addedAnswer.id)
        Assertions.assertThat(getAnswer.name).isEqualTo(answer.name)
        Assertions.assertThat(getAnswer.name).isEqualTo(addedAnswer.name)
    }

    @Test
    fun `get non-existing answer`() {
        // Arrange

        // Act
        val getAnswerResponse = answerClient.get(UUID.randomUUID()).block()

        // Assert
        Assertions.assertThat(getAnswerResponse).isNull()
    }

    @Test
    fun `update answer`() {
        // Arrange
        val addAnswerRequest = AddAnswerRequest("Original Name")
        val addedAnswer = answerClient.add(addAnswerRequest).block()
        val updateAnswerRequest = UpdateAnswerRequest("Updated Name")

        // Act
        val updatedAnswer = answerClient.update(addedAnswer.id, updateAnswerRequest).block()
        val getAnswer = answerClient.get(addedAnswer.id).block()

        // Assert
        Assertions.assertThat(updatedAnswer.id).isEqualTo(addedAnswer.id)
        Assertions.assertThat(getAnswer.id).isEqualTo(addedAnswer.id)
        Assertions.assertThat(updatedAnswer.name).isEqualTo(updateAnswerRequest.name)
    }

    @Test
    fun `update non-existing answer`() {
        // Arrange
        val updateAnswerRequest = UpdateAnswerRequest("Updated Name")

        // Act
        val getAnswerResponse = answerClient.update(UUID.randomUUID(), updateAnswerRequest).block()

        // Assert
        Assertions.assertThat(getAnswerResponse).isNull()
    }

    @Test
    fun `list answers`() {
        // Arrange
        answerProvider().forEach {
            answerClient.add(AddAnswerRequest(it)).block()
        }

        // Act
        val getAnswers = answerClient.list()

        // Assert
        Assertions.assertThat(getAnswers).extracting<String> { x -> x.name }
            .containsAll(answerProvider().map { it.name }.toList())
    }

    @Test
    fun `get VIPs`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:SECRET_PASSWORD".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val getAnswers = answerClient.getVIPs(basicAuthenticationHeader)

        // Assert
        Assertions.assertThat(getAnswers).anyMatch { it.name == "Hermione Granger" }
        Assertions.assertThat(getAnswers).anyMatch { it.name == "Harry Potter" }
        Assertions.assertThat(getAnswers).anyMatch { it.name == "Ronald Weasley" }
    }

    @Test
    fun `fail get VIPs with bad authentication`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:wrongPassword".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val thrown = Assertions.catchThrowable {
            answerClient.getVIPs(basicAuthenticationHeader)
        }

        // Assert
        Assertions.assertThat(thrown)
            .isInstanceOf(HttpClientResponseException::class.java)
            .hasMessageContaining("Unauthorized")
    }

    fun answerProvider() = TestDataProvider.answerProvider()
}