package de.debuglevel.responder.question

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
        Assertions.assertThat(addedQuestion.title).isEqualTo(question.title)
        Assertions.assertThat(addedQuestion.title).isEqualTo(addQuestionRequest.title)
    }

    @ParameterizedTest
    @MethodSource("questionProvider")
    fun `get question`(question: Question) {
        // Arrange
        val addQuestionRequest = AddQuestionRequest(question)
        val addedQuestion = questionClient.add(addQuestionRequest).block()

        // Act
        val gotQuestion = questionClient.get(addedQuestion.id).block()

        // Assert
        Assertions.assertThat(gotQuestion.id).isEqualTo(addedQuestion.id)
        Assertions.assertThat(gotQuestion.title).isEqualTo(question.title)
        Assertions.assertThat(gotQuestion.title).isEqualTo(addedQuestion.title)
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
        val gotQuestion = questionClient.get(addedQuestion.id).block()

        // Assert
        Assertions.assertThat(updatedQuestion.id).isEqualTo(addedQuestion.id)
        Assertions.assertThat(gotQuestion.id).isEqualTo(addedQuestion.id)
        Assertions.assertThat(updatedQuestion.title).isEqualTo(updateQuestionRequest.title)
    }

    @Test
    fun `update non-existing question`() {
        // Arrange
        val updateQuestionRequest = UpdateQuestionRequest("Updated Name")

        // Act
        val updateQuestionResponse = questionClient.update(UUID.randomUUID(), updateQuestionRequest).block()

        // Assert
        Assertions.assertThat(updateQuestionResponse).isNull()
    }

    @Test
    fun `get questions`() {
        // Arrange
        questionProvider().forEach {
            questionClient.add(AddQuestionRequest(it)).block()
        }

        // Act
        val gotQuestions = questionClient.getAll()

        // Assert
        Assertions.assertThat(gotQuestions).extracting<String> { x -> x.title }
            .containsAll(questionProvider().map { it.title }.toList())
    }

    fun questionProvider() = TestDataProvider.questionProvider()
}