package de.debuglevel.responder.question

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
class QuestionControllerTests {
    @Inject
    lateinit var questionClient: QuestionClient

    @ParameterizedTest
    @MethodSource("questionProvider")
    fun `add question`(question: Question) {
        // Arrange
        val addQuestionRequest = AddQuestionRequest(question)

        // Act
        val addedQuestion = questionClient.add(addQuestionRequest).block()

        // Assert
        Assertions.assertThat(addedQuestion.name).isEqualTo(question.name)
        Assertions.assertThat(addedQuestion.name).isEqualTo(addQuestionRequest.name)
    }

    @ParameterizedTest
    @MethodSource("questionProvider")
    fun `get question`(question: Question) {
        // Arrange
        val addQuestionRequest = AddQuestionRequest(question)
        val addedQuestion = questionClient.add(addQuestionRequest).block()

        // Act
        val getQuestion = questionClient.get(addedQuestion.id).block()

        // Assert
        Assertions.assertThat(getQuestion.id).isEqualTo(addedQuestion.id)
        Assertions.assertThat(getQuestion.name).isEqualTo(question.name)
        Assertions.assertThat(getQuestion.name).isEqualTo(addedQuestion.name)
    }

    @Test
    fun `get non-existing question`() {
        // Arrange

        // Act
        val getQuestionResponse = questionClient.get(UUID.randomUUID()).block()

        // Assert
        Assertions.assertThat(getQuestionResponse).isNull()
    }

    @Test
    fun `update question`() {
        // Arrange
        val addQuestionRequest = AddQuestionRequest("Original Name")
        val addedQuestion = questionClient.add(addQuestionRequest).block()
        val updateQuestionRequest = UpdateQuestionRequest("Updated Name")

        // Act
        val updatedQuestion = questionClient.update(addedQuestion.id, updateQuestionRequest).block()
        val getQuestion = questionClient.get(addedQuestion.id).block()

        // Assert
        Assertions.assertThat(updatedQuestion.id).isEqualTo(addedQuestion.id)
        Assertions.assertThat(getQuestion.id).isEqualTo(addedQuestion.id)
        Assertions.assertThat(updatedQuestion.name).isEqualTo(updateQuestionRequest.name)
    }

    @Test
    fun `update non-existing question`() {
        // Arrange
        val updateQuestionRequest = UpdateQuestionRequest("Updated Name")

        // Act
        val getQuestionResponse = questionClient.update(UUID.randomUUID(), updateQuestionRequest).block()

        // Assert
        Assertions.assertThat(getQuestionResponse).isNull()
    }

    @Test
    fun `list questions`() {
        // Arrange
        questionProvider().forEach {
            questionClient.add(AddQuestionRequest(it)).block()
        }

        // Act
        val getQuestions = questionClient.list()

        // Assert
        Assertions.assertThat(getQuestions).extracting<String> { x -> x.name }
            .containsAll(questionProvider().map { it.name }.toList())
    }

    @Test
    fun `get VIPs`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:SECRET_PASSWORD".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val getQuestions = questionClient.getVIPs(basicAuthenticationHeader)

        // Assert
        Assertions.assertThat(getQuestions).anyMatch { it.name == "Hermione Granger" }
        Assertions.assertThat(getQuestions).anyMatch { it.name == "Harry Potter" }
        Assertions.assertThat(getQuestions).anyMatch { it.name == "Ronald Weasley" }
    }

    @Test
    fun `fail get VIPs with bad authentication`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:wrongPassword".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val thrown = Assertions.catchThrowable {
            questionClient.getVIPs(basicAuthenticationHeader)
        }

        // Assert
        Assertions.assertThat(thrown)
            .isInstanceOf(HttpClientResponseException::class.java)
            .hasMessageContaining("Unauthorized")
    }

    fun questionProvider() = TestDataProvider.questionProvider()
}