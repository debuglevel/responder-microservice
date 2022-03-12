package de.debuglevel.responder.question

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuestionServiceTests {

    @Inject
    lateinit var questionService: QuestionService

    @ParameterizedTest
    @MethodSource("questionProvider")
    fun `add question`(question: Question) {
        // Arrange

        // Act
        val addedQuestion = questionService.add(question)

        // Assert
        assertThat(addedQuestion).isEqualTo(question)
    }

    @ParameterizedTest
    @MethodSource("questionProvider")
    fun `get question`(question: Question) {
        // Arrange
        val addedQuestion = questionService.add(question)

        // Act
        val gotQuestion = questionService.get(addedQuestion.id!!)

        // Assert
        assertThat(gotQuestion).isEqualTo(addedQuestion)
    }

    @ParameterizedTest
    @MethodSource("questionProvider")
    fun `question exists`(question: Question) {
        // Arrange
        val addedQuestion = questionService.add(question)

        // Act
        val questionExists = questionService.exists(addedQuestion.id!!)

        // Assert
        assertThat(questionExists).isTrue
    }

    @Test
    fun `count questions`() {
        val questions = questionProvider().toList()

        val initialQuestionCount = questionService.count

        questions.forEachIndexed { index, question ->
            // Arrange
            questionService.add(question)

            // Act
            val questionCount = questionService.count

            // Assert
            assertThat(questionCount).isEqualTo(initialQuestionCount + index + 1)
        }
    }

    @Test
    fun `update question`() {
        // Arrange
        val question = Question(null, "Test")
        val addedQuestion = questionService.add(question)

        // Act
        val gotQuestion = questionService.get(addedQuestion.id!!)
        gotQuestion.title = "Test updated"
        val updatedQuestion = questionService.update(gotQuestion.id!!, gotQuestion)

        // Assert
        assertThat(updatedQuestion.title).isEqualTo("Test updated")
    }

    /**
     * Test updating a copy of the original entity, because this way it's ensured that the service can handle detached entities.
     */
    @Test
    fun `update question with copy()`() {
        // Arrange
        val question = Question(null, "Test")
        val addedQuestion = questionService.add(question)

        // Act
        val gotQuestion = questionService.get(addedQuestion.id!!)
        val updateQuestion = gotQuestion.copy(title = "Test updated")
        val updatedQuestion = questionService.update(updateQuestion.id!!, updateQuestion)

        // Assert
        assertThat(updatedQuestion.title).isEqualTo("Test updated")
    }

    @Test
    fun `delete question`() {
        // Arrange
        val question = Question(null, "Test")
        val addedQuestion = questionService.add(question)
        val questionCount = questionService.count

        // Act
        questionService.delete(addedQuestion.id!!)
        val questionExists = questionService.exists(addedQuestion.id!!)
        val questionCountAfterDeletion = questionService.count

        // Assert
        assertThat(questionExists).isFalse
        assertThat(questionCountAfterDeletion).isEqualTo(questionCount - 1)
    }

    @Test
    fun `delete all questions`() {
        val questions = questionProvider().toList()

        // Arrange
        for (question in questions) {
            questionService.add(question)
        }

        // Act
        questionService.deleteAll()
        val questionCountAfterDeletion = questionService.count

        // Assert
        assertThat(questionCountAfterDeletion).isEqualTo(0)
    }

    fun questionProvider() = TestDataProvider.questionProvider()
}