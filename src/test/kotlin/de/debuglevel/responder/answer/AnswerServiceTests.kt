package de.debuglevel.responder.answer

import de.debuglevel.responder.question.Question
import de.debuglevel.responder.question.QuestionService
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

    @Inject
    lateinit var questionService: QuestionService

    @ParameterizedTest
    @MethodSource("answerProvider")
    fun `add answer`(answer: Answer) {
        // Arrange
        val addedQuestion = questionService.add(answer.question)
        val answerWithPersistedQuestion = answer.copy(question = addedQuestion)

        // Act
        val addedAnswer = answerService.add(answerWithPersistedQuestion)

        // Assert
        assertThat(addedAnswer).isEqualTo(answer)
    }

    @ParameterizedTest
    @MethodSource("answerProvider")
    fun `get answer`(answer: Answer) {
        // Arrange
        val addedQuestion = questionService.add(answer.question)
        val answerWithPersistedQuestion = answer.copy(question = addedQuestion)
        val addedAnswer = answerService.add(answerWithPersistedQuestion)

        // Act
        val gotAnswer = answerService.get(addedAnswer.id!!)

        // Assert
        assertThat(gotAnswer).isEqualTo(addedAnswer)
    }

    @ParameterizedTest
    @MethodSource("answerProvider")
    fun `answer exists`(answer: Answer) {
        // Arrange
        val addedQuestion = questionService.add(answer.question)
        val answerWithPersistedQuestion = answer.copy(question = addedQuestion)
        val addedAnswer = answerService.add(answerWithPersistedQuestion)

        // Act
        val answerExists = answerService.exists(addedAnswer.id!!)

        // Assert
        assertThat(answerExists).isTrue
    }

    @Test
    fun `count answers`() {
        val answers = answerProvider().toList()

        val initialAnswerCount = answerService.count

        answers.forEachIndexed { index, answer ->
            // Arrange
            val addedQuestion = questionService.add(answer.question)
            val answerWithPersistedQuestion = answer.copy(question = addedQuestion)
            val addedAnswer = answerService.add(answerWithPersistedQuestion)

            // Act
            val answerCount = answerService.count

            // Assert
            assertThat(answerCount).isEqualTo(initialAnswerCount + index + 1)
        }
    }

    @Test
    fun `update answer`() {
        // Arrange
        val question = questionService.add(Question(null, "Title"))
        val answer = Answer(null, "Test", question)
        val addedAnswer = answerService.add(answer)

        // Act
        val gotAnswer = answerService.get(addedAnswer.id!!)
        val updatedAnswer = answerService.update(gotAnswer.id!!, "Test updated")

        // Assert
        assertThat(updatedAnswer.title).isEqualTo("Test updated")
    }

//    /**
//     * Test updating a copy of the original entity, because this way it's ensured that the service can handle detached entities.
//     */
//    @Test
//    fun `update answer with copy()`() {
//        // Arrange
//        val question = questionService.add(Question(null, "Title"))
//        val answer = Answer(null, "Test", question)
//        val addedAnswer = answerService.add(answer)
//
//        // Act
//        val gotAnswer = answerService.get(addedAnswer.id!!)
//        val updateAnswer = gotAnswer.copy(title = "Test updated")
//        val updatedAnswer = answerService.update(updateAnswer.id!!, updateAnswer)
//
//        // Assert
//        assertThat(updatedAnswer.title).isEqualTo("Test updated")
//    }

    @Test
    fun `delete answer`() {
        // Arrange
        val question = questionService.add(Question(null, "Title"))
        val answer = Answer(null, "Test", question)
        val addedAnswer = answerService.add(answer)
        val answerCount = answerService.count

        // Act
        answerService.delete(addedAnswer.id!!)
        val answerExists = answerService.exists(addedAnswer.id!!)
        val answerCountAfterDeletion = answerService.count

        // Assert
        assertThat(answerExists).isFalse
        assertThat(answerCountAfterDeletion).isEqualTo(answerCount - 1)
    }

    @Test
    fun `delete all answers`() {
        val answers = answerProvider().toList()

        // Arrange
        for (answer in answers) {
            val addedQuestion = questionService.add(answer.question)
            val answerWithPersistedQuestion = answer.copy(question = addedQuestion)
            val addedAnswer = answerService.add(answerWithPersistedQuestion)
        }

        // Act
        answerService.deleteAll()
        val answerCountAfterDeletion = answerService.count

        // Assert
        assertThat(answerCountAfterDeletion).isEqualTo(0)
    }

    fun answerProvider() = TestDataProvider.answerProvider()
}