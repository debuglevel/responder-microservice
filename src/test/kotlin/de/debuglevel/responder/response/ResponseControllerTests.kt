package de.debuglevel.responder.response

import de.debuglevel.responder.AuthenticationUtils
import de.debuglevel.responder.answer.AddAnswerRequest
import de.debuglevel.responder.answer.Answer
import de.debuglevel.responder.answer.AnswerClient
import de.debuglevel.responder.answer.UpdateAnswerRequest
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
class ResponseControllerTests {
    @Inject
    lateinit var responseClient: ResponseClient

    @Inject
    lateinit var answerClient: AnswerClient

    @Inject
    lateinit var questionClient: QuestionClient

    private val authHeader = AuthenticationUtils.getBasicAuthenticationHeader()

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `add response`(response: Response) {
        // Arrange
        val addQuestionRequest = AddQuestionRequest(response.question)
        val addQuestionResponse = questionClient.add(addQuestionRequest, authHeader).block()
        val question = addQuestionResponse.toQuestion()

        val answer_ = response.answer?.copy(question = question)!!
        val addAnswerRequest = AddAnswerRequest(answer_)
        val addAnswerResponse = answerClient.add(addAnswerRequest, authHeader).block()

        val response_ = response.copy(question = question, answer = addAnswerResponse.toAnswer(question))
        val addResponseRequest = AddResponseRequest(response_)

        // Act
        val addResponseResponse = responseClient.add(addResponseRequest, authHeader).block()

        // Assert
        Assertions.assertThat(addResponseResponse.token).isEqualTo(response.token)
        Assertions.assertThat(addResponseResponse.token).isEqualTo(addResponseRequest.token)
    }

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `get response`(response: Response) {
        // Arrange
        val addQuestionRequest = AddQuestionRequest(response.question)
        val addQuestionResponse = questionClient.add(addQuestionRequest, authHeader).block()
        val question = addQuestionResponse.toQuestion()

        val answer_ = response.answer?.copy(question = question)!!
        val addAnswerRequest = AddAnswerRequest(answer_)
        val addAnswerResponse = answerClient.add(addAnswerRequest, authHeader).block()

        val response_ = response.copy(question = question, answer = addAnswerResponse.toAnswer(question))
        val addResponseRequest = AddResponseRequest(response_)
        val addResponseResponse = responseClient.add(addResponseRequest, authHeader).block()

        // Act
        val getResponseResponse = responseClient.get(addResponseResponse.id).block()

        // Assert
        Assertions.assertThat(getResponseResponse.id).isEqualTo(addResponseResponse.id)
        Assertions.assertThat(getResponseResponse.token).isEqualTo(response.token)
        Assertions.assertThat(getResponseResponse.token).isEqualTo(addResponseResponse.token)
    }

    @Test
    fun `get non-existing response`() {
        // Arrange

        // Act
        val getResponseResponse = responseClient.get(UUID.randomUUID()).block()

        // Assert
        Assertions.assertThat(getResponseResponse).isNull()
    }

    @Test
    fun `update response`() {
        // Arrange
        val addQuestionRequest = AddQuestionRequest("Whatever")
        val addQuestionResponse = questionClient.add(addQuestionRequest, authHeader).block()
        val question = addQuestionResponse.toQuestion()

        val answer = Answer(null, "Original", addQuestionResponse.toQuestion())
        val addAnswerRequest = AddAnswerRequest(answer)
        val addAnswerResponse = answerClient.add(addAnswerRequest, authHeader).block()
        val addedAnswer = addAnswerResponse.toAnswer(question)

        val response = Response(null, "Original", question)
        val addResponseRequest = AddResponseRequest(response)
        val addResponseResponse = responseClient.add(addResponseRequest, authHeader).block()
        val addedResponse = addResponseResponse.toResponse(question)

        // Act
        val updateResponseRequest = UpdateResponseRequest("Updated", addedAnswer.id!!)
        val updateResponseResponse = responseClient.update(addResponseResponse.id, updateResponseRequest, authHeader).block()
        val getResponseResponse = responseClient.get(addResponseResponse.id).block()

        // Assert
        Assertions.assertThat(updateResponseResponse.id).isEqualTo(addResponseResponse.id)
        Assertions.assertThat(getResponseResponse.id).isEqualTo(addResponseResponse.id)

        Assertions.assertThat(updateResponseResponse.token).isEqualTo(updateResponseRequest.token)

        Assertions.assertThat(updateResponseRequest.answerId).isEqualTo(addedAnswer.id!!)
        Assertions.assertThat(updateResponseResponse.answerId).isEqualTo(addedAnswer.id!!)
        Assertions.assertThat(getResponseResponse.answerId).isEqualTo(addedAnswer.id!!)
    }

    @Test
    fun `update non-existing response`() {
        // Arrange
        val updateResponseRequest = UpdateResponseRequest("Updated", UUID.randomUUID())

        // Act
        val updateResponseResponse = responseClient.update(UUID.randomUUID(), updateResponseRequest, authHeader).block()

        // Assert
        Assertions.assertThat(updateResponseResponse).isNull()
    }

    @Test
    fun `get responses`() {
        // Arrange
        responseProvider().forEach {
            // Arrange
            val addQuestionRequest = AddQuestionRequest(it.question)
            val addQuestionResponse = questionClient.add(addQuestionRequest, authHeader).block()

            val response_ = it.copy(question = addQuestionResponse.toQuestion())
            val addResponseRequest = AddResponseRequest(response_)
            val addResponseResponse = responseClient.add(addResponseRequest, authHeader).block()
        }

        // Act
        val getResponseResponses = responseClient.getAll(authHeader)

        // Assert
        Assertions.assertThat(getResponseResponses).extracting<String> { x -> x.token }
            .containsAll(responseProvider().map { it.token }.toList())
    }

    fun responseProvider() = TestDataProvider.responseProvider()
}