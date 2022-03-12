package de.debuglevel.responder.answer

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnswerServiceTests {

    @Inject
    lateinit var answerService: AnswerService

    @ParameterizedTest
    @MethodSource("answerProvider")
    fun `save answer`(answer: Answer) {
        // Arrange

        // Act
        val savedAnswer = answerService.add(answer)

        // Assert
        assertThat(savedAnswer).isEqualTo(answer)
    }

    @ParameterizedTest
    @MethodSource("answerProvider")
    fun `retrieve answer`(answer: Answer) {
        // Arrange
        val savedAnswer = answerService.add(answer)

        // Act
        val retrievedAnswer = answerService.get(savedAnswer.id!!)

        // Assert
        assertThat(retrievedAnswer).isEqualTo(savedAnswer)
    }

    @Test
    fun `update answer`() {
        // Arrange
        val answer = Answer(null, "Test")
        val savedAnswer = answerService.add(answer)

        // Act
        val retrievedAnswer = answerService.get(savedAnswer.id!!)
        retrievedAnswer.name = "Test updated"
        val updatedAnswer = answerService.update(retrievedAnswer.id!!, retrievedAnswer)

        // Assert
        assertThat(updatedAnswer.name).isEqualTo("Test updated")
    }

    /**
     * Test updating a copy of the original entity, because this way it's ensured that the service can handle detached entities.
     */
    @Test
    fun `update answer with copy()`() {
        // Arrange
        val answer = Answer(null, "Test")
        val savedAnswer = answerService.add(answer)

        // Act
        val retrievedAnswer = answerService.get(savedAnswer.id!!)
        val updateAnswer = retrievedAnswer.copy(name = "Test updated")
        val updatedAnswer = answerService.update(updateAnswer.id!!, updateAnswer)

        // Assert
        assertThat(updatedAnswer.name).isEqualTo("Test updated")
    }

    fun answerProvider() = TestDataProvider.answerProvider()
}