package de.debuglevel.responder.question

import jakarta.inject.Singleton
import mu.KotlinLogging
import java.util.*

@Singleton
class QuestionService(
    private val questionRepository: QuestionRepository,
) {
    private val logger = KotlinLogging.logger {}

    val count: Long
        get() {
            logger.debug { "Getting questions count..." }

            val count = questionRepository.count()

            logger.debug { "Got questions count: $count" }
            return count
        }

    fun exists(id: UUID): Boolean {
        logger.debug { "Checking if question $id exists..." }

        val isExisting = questionRepository.existsById(id)

        logger.debug { "Checked if question $id exists: $isExisting" }
        return isExisting
    }

    fun get(id: UUID): Question {
        logger.debug { "Getting question with ID '$id'..." }

        val question: Question = questionRepository.findById(id).orElseThrow {
            logger.debug { "Getting question with ID '$id' failed" }
            ItemNotFoundException(id)
        }

        logger.debug { "Got question with ID '$id': $question" }
        return question
    }

    fun getAll(): Set<Question> {
        logger.debug { "Getting all questions..." }

        val questions = questionRepository.findAll().toSet()

        logger.debug { "Got ${questions.size} questions" }
        return questions
    }

    fun add(question: Question): Question {
        logger.debug { "Adding question '$question'..." }

        val savedQuestion = questionRepository.save(question)

        logger.debug { "Added question: $savedQuestion" }
        return savedQuestion
    }

    fun update(id: UUID, question: Question): Question {
        logger.debug { "Updating question '$question' with ID '$id'..." }

        // an object must be known to Hibernate (i.e. retrieved first) to get updated;
        // it would be a "detached entity" otherwise.
        val updateQuestion = this.get(id).apply {
            title = question.title
        }

        val updatedQuestion = questionRepository.update(updateQuestion)

        logger.debug { "Updated question: $updatedQuestion with ID '$id'" }
        return updatedQuestion
    }

    fun delete(id: UUID) {
        logger.debug { "Deleting question with ID '$id'..." }

        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id)
        } else {
            throw ItemNotFoundException(id)
        }

        logger.debug { "Deleted question with ID '$id'" }
    }

    fun deleteAll() {
        logger.debug { "Deleting all questions..." }

        val countBefore = questionRepository.count()
        //questionRepository.deleteAll() // CAVEAT: does not delete dependent entities; use this instead: questionRepository.findAll().forEach { questionRepository.delete(it) }
        questionRepository.findAll().forEach { questionRepository.delete(it) }
        val countAfter = questionRepository.count()
        val countDeleted = countBefore - countAfter

        logger.debug { "Deleted $countDeleted of $countBefore questions, $countAfter remaining" }
    }

    class ItemNotFoundException(criteria: Any) : Exception("Item '$criteria' does not exist.")
}