package de.debuglevel.responder.response

import de.debuglevel.responder.answer.AnswerService
import de.debuglevel.responder.question.Question
import de.debuglevel.responder.question.QuestionService
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.assertj.core.data.TemporalUnitOffset
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResponseServiceTests {

    @Inject
    lateinit var responseService: ResponseService

    @Inject
    lateinit var answerService: AnswerService

    @Inject
    lateinit var questionService: QuestionService

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `add response`(response: Response) {
        // Arrange
        val addedQuestion = questionService.add(response.question)
        val responseWithPersistedQuestion = response.copy(question = addedQuestion)

        // Act
        val addedResponse = responseService.add(responseWithPersistedQuestion)

        // Assert
        assertThat(addedResponse).isEqualTo(response)
    }

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `get response`(response: Response) {
        // Arrange
        val addedQuestion = questionService.add(response.question)
        val responseWithPersistedQuestion = response.copy(question = addedQuestion)
        val addedResponse = responseService.add(responseWithPersistedQuestion)

        // Act
        val gotResponse = responseService.get(addedResponse.id!!)

        // Assert
        assertThat(gotResponse).isEqualTo(addedResponse)
    }

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `response exists`(response: Response) {
        // Arrange
        val addedQuestion = questionService.add(response.question)
        val responseWithPersistedQuestion = response.copy(question = addedQuestion)
        val addedResponse = responseService.add(responseWithPersistedQuestion)

        // Act
        val responseExists = responseService.exists(addedResponse.id!!)

        // Assert
        assertThat(responseExists).isTrue
    }

    @Test
    fun `count responses`() {
        val responses = responseProvider().toList()

        val initialResponseCount = responseService.count

        responses.forEachIndexed { index, response ->
            // Arrange
            val addedQuestion = questionService.add(response.question)
            val responseWithPersistedQuestion = response.copy(question = addedQuestion)
            val addedResponse = responseService.add(responseWithPersistedQuestion)

            // Act
            val responseCount = responseService.count

            // Assert
            assertThat(responseCount).isEqualTo(initialResponseCount + index + 1)
        }
    }

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `submit answer to response`(response: Response) {
        // Arrange
        val addedQuestion = questionService.add(response.question)

        val answerWithPersistedQuestion = response.answer!!.copy(question = addedQuestion)
        val addedAnswer = answerService.add(answerWithPersistedQuestion)

        val responseWithPersistedQuestion = response.copy(question = addedQuestion)
        val addedResponse = responseService.add(responseWithPersistedQuestion)

        // Act
        val gotResponse = responseService.get(addedResponse.id!!)
        val updatedResponse = responseService.submitAnswer(gotResponse.id!!, addedAnswer)

        // Assert
        assertThat(updatedResponse.answer).isEqualTo(addedAnswer)
        assertThat(updatedResponse.answeredOn).isCloseToUtcNow(within(1, ChronoUnit.SECONDS))
    }

//    /**
//     * Test updating a copy of the original entity, because this way it's ensured that the service can handle detached entities.
//     */
//    @Test
//    fun `update response with copy()`() {
//        // Arrange
//        val question = questionService.add(Question(null, "Title"))
//        val response = Response(null, "Test", question)
//        val addedResponse = responseService.add(response)
//
//        // Act
//        val gotResponse = responseService.get(addedResponse.id!!)
//        val updateResponse = gotResponse.copy(title = "Test updated")
//        val updatedResponse = responseService.update(updateResponse.id!!, updateResponse)
//
//        // Assert
//        assertThat(updatedResponse.title).isEqualTo("Test updated")
//    }

    @Test
    fun `delete response`() {
        // Arrange
        val question = questionService.add(Question(null, "Title"))
        val response = Response(null, "Test", question)
        val addedResponse = responseService.add(response)
        val responseCount = responseService.count

        // Act
        responseService.delete(addedResponse.id!!)
        val responseExists = responseService.exists(addedResponse.id!!)
        val responseCountAfterDeletion = responseService.count

        // Assert
        assertThat(responseExists).isFalse
        assertThat(responseCountAfterDeletion).isEqualTo(responseCount - 1)
    }

    @Test
    fun `delete all responses`() {
        val responses = responseProvider().toList()

        // Arrange
        for (response in responses) {
            val addedQuestion = questionService.add(response.question)
            val responseWithPersistedQuestion = response.copy(question = addedQuestion)
            val addedResponse = responseService.add(responseWithPersistedQuestion)
        }

        // Act
        responseService.deleteAll()
        val responseCountAfterDeletion = responseService.count

        // Assert
        assertThat(responseCountAfterDeletion).isEqualTo(0)
    }

    fun responseProvider() = TestDataProvider.responseProvider()
}