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
    fun `save question`(question: Question) {
        // Arrange

        // Act
        val savedQuestion = questionService.add(question)

        // Assert
        assertThat(savedQuestion).isEqualTo(question)
    }

    @ParameterizedTest
    @MethodSource("questionProvider")
    fun `retrieve question`(question: Question) {
        // Arrange
        val savedQuestion = questionService.add(question)

        // Act
        val retrievedQuestion = questionService.get(savedQuestion.id!!)

        // Assert
        assertThat(retrievedQuestion).isEqualTo(savedQuestion)
    }

    @Test
    fun `update question`() {
        // Arrange
        val question = Question(null, "Test")
        val savedQuestion = questionService.add(question)

        // Act
        val retrievedQuestion = questionService.get(savedQuestion.id!!)
        retrievedQuestion.name = "Test updated"
        val updatedQuestion = questionService.update(retrievedQuestion.id!!, retrievedQuestion)

        // Assert
        assertThat(updatedQuestion.name).isEqualTo("Test updated")
    }

    /**
     * Test updating a copy of the original entity, because this way it's ensured that the service can handle detached entities.
     */
    @Test
    fun `update question with copy()`() {
        // Arrange
        val question = Question(null, "Test")
        val savedQuestion = questionService.add(question)

        // Act
        val retrievedQuestion = questionService.get(savedQuestion.id!!)
        val updateQuestion = retrievedQuestion.copy(name = "Test updated")
        val updatedQuestion = questionService.update(updateQuestion.id!!, updateQuestion)

        // Assert
        assertThat(updatedQuestion.name).isEqualTo("Test updated")
    }

    fun questionProvider() = TestDataProvider.questionProvider()
}