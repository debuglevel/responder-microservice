package de.debuglevel.responder.answer

import de.debuglevel.responder.AuthenticationUtils
import de.debuglevel.responder.question.AddQuestionRequest
import de.debuglevel.responder.question.QuestionClient
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

    @Inject
    lateinit var questionClient: QuestionClient

    private val authHeader = AuthenticationUtils.getBasicAuthenticationHeader()

    @ParameterizedTest
    @MethodSource("answerProvider")
    fun `add answer`(answer: Answer) {
        // Arrange
        val addQuestionRequest = AddQuestionRequest(answer.question)
        val addQuestionResponse = questionClient.add(addQuestionRequest, authHeader).block()
        val answer_ = answer.copy(question = addQuestionResponse.toQuestion())
        val addAnswerRequest = AddAnswerRequest(answer_)

        // Act
        val addAnswerResponse = answerClient.add(addAnswerRequest, authHeader).block()

        // Assert
        Assertions.assertThat(addAnswerResponse.title).isEqualTo(answer.title)
        Assertions.assertThat(addAnswerResponse.title).isEqualTo(addAnswerRequest.title)
    }

    @ParameterizedTest
    @MethodSource("answerProvider")
    fun `get answer`(answer: Answer) {
        // Arrange
        val addQuestionRequest = AddQuestionRequest(answer.question)
        val addQuestionResponse = questionClient.add(addQuestionRequest, authHeader).block()
        val answer_ = answer.copy(question = addQuestionResponse.toQuestion())
        val addAnswerRequest = AddAnswerRequest(answer_)
        val addAnswerResponse = answerClient.add(addAnswerRequest, authHeader).block()

        // Act
        val getAnswerResponse = answerClient.get(addAnswerResponse.id).block()

        // Assert
        Assertions.assertThat(getAnswerResponse.id).isEqualTo(addAnswerResponse.id)
        Assertions.assertThat(getAnswerResponse.title).isEqualTo(answer.title)
        Assertions.assertThat(getAnswerResponse.title).isEqualTo(addAnswerResponse.title)
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
        val addQuestionRequest = AddQuestionRequest("Whatever")
        val addQuestionResponse = questionClient.add(addQuestionRequest, authHeader).block()
        val answer = Answer(null, "Original", addQuestionResponse.toQuestion())
        val addAnswerRequest = AddAnswerRequest(answer)

        val addAnswerResponse = answerClient.add(addAnswerRequest, authHeader).block()
        val updateAnswerRequest = UpdateAnswerRequest("Updated")

        // Act
        val updateAnswerResponse = answerClient.update(addAnswerResponse.id, updateAnswerRequest, authHeader).block()
        val getAnswerResponse = answerClient.get(addAnswerResponse.id).block()

        // Assert
        Assertions.assertThat(updateAnswerResponse.id).isEqualTo(addAnswerResponse.id)
        Assertions.assertThat(getAnswerResponse.id).isEqualTo(addAnswerResponse.id)
        Assertions.assertThat(updateAnswerResponse.title).isEqualTo(updateAnswerRequest.title)
    }

    @Test
    fun `update non-existing answer`() {
        // Arrange
        val updateAnswerRequest = UpdateAnswerRequest("Updated")

        // Act
        val updateAnswerResponse = answerClient.update(UUID.randomUUID(), updateAnswerRequest, authHeader).block()

        // Assert
        Assertions.assertThat(updateAnswerResponse).isNull()
    }

    @Test
    fun `get answers`() {
        // Arrange
        answerProvider().forEach {
            // Arrange
            val addQuestionRequest = AddQuestionRequest(it.question)
            val addQuestionResponse = questionClient.add(addQuestionRequest, authHeader).block()
            val answer_ = it.copy(question = addQuestionResponse.toQuestion())
            val addAnswerRequest = AddAnswerRequest(answer_)
            val addAnswerResponse = answerClient.add(addAnswerRequest, authHeader).block()
        }

        // Act
        val getAnswerResponses = answerClient.getAll(authHeader)

        // Assert
        Assertions.assertThat(getAnswerResponses).extracting<String> { x -> x.title }
            .containsAll(answerProvider().map { it.title }.toList())
    }

    fun answerProvider() = TestDataProvider.answerProvider()
}