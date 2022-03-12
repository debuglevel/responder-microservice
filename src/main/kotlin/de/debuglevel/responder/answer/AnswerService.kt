package de.debuglevel.responder.answer

import jakarta.inject.Singleton
import mu.KotlinLogging
import java.util.*

@Singleton
class AnswerService(
    private val answerRepository: AnswerRepository,
) {
    private val logger = KotlinLogging.logger {}

    val count: Long
        get() {
            logger.debug { "Getting answers count..." }

            val count = answerRepository.count()

            logger.debug { "Got answers count: $count" }
            return count
        }

    fun exists(id: UUID): Boolean {
        logger.debug { "Checking if answer $id exists..." }

        val isExisting = answerRepository.existsById(id)

        logger.debug { "Checked if answer $id exists: $isExisting" }
        return isExisting
    }

    fun get(id: UUID): Answer {
        logger.debug { "Getting answer with ID '$id'..." }

        val answer: Answer = answerRepository.findById(id).orElseThrow {
            logger.debug { "Getting answer with ID '$id' failed" }
            ItemNotFoundException(id)
        }

        logger.debug { "Got answer with ID '$id': $answer" }
        return answer
    }

    fun getAll(): Set<Answer> {
        logger.debug { "Getting all answers..." }

        val answers = answerRepository.findAll().toSet()

        logger.debug { "Got ${answers.size} answers" }
        return answers
    }

    fun add(answer: Answer): Answer {
        logger.debug { "Adding answer '$answer'..." }

        val savedAnswer = answerRepository.save(answer)

        logger.debug { "Added answer: $savedAnswer" }
        return savedAnswer
    }

//    fun update(id: UUID, answer: Answer): Answer {
//        logger.debug { "Updating answer '$answer' with ID '$id'..." }
//
//        // an object must be known to Hibernate (i.e. retrieved first) to get updated;
//        // it would be a "detached entity" otherwise.
//        val updateAnswer = this.get(id).apply {
//            title = answer.title
//        }
//
//        val updatedAnswer = answerRepository.update(updateAnswer)
//
//        logger.debug { "Updated answer: $updatedAnswer with ID '$id'" }
//        return updatedAnswer
//    }

    fun update(id: UUID, updatedTitle: String): Answer {
        logger.debug { "Updating answer with ID '$id'..." }

        // an object must be known to Hibernate (i.e. retrieved first) to get updated;
        // it would be a "detached entity" otherwise.
        val updateAnswer = this.get(id).apply {
            title = updatedTitle
        }

        val updatedAnswer = answerRepository.update(updateAnswer)

        logger.debug { "Updated answer: $updatedAnswer with ID '$id'" }
        return updatedAnswer
    }

    fun delete(id: UUID) {
        logger.debug { "Deleting answer with ID '$id'..." }

        if (answerRepository.existsById(id)) {
            answerRepository.deleteById(id)
        } else {
            throw ItemNotFoundException(id)
        }

        logger.debug { "Deleted answer with ID '$id'" }
    }

    fun deleteAll() {
        logger.debug { "Deleting all answers..." }

        val countBefore = answerRepository.count()
        answerRepository.deleteAll() // CAVEAT: does not delete dependent entities; use this instead: answerRepository.findAll().forEach { answerRepository.delete(it) }
        val countAfter = answerRepository.count()
        val countDeleted = countBefore - countAfter

        logger.debug { "Deleted $countDeleted of $countBefore answers, $countAfter remaining" }
    }

    class ItemNotFoundException(criteria: Any) : Exception("Item '$criteria' does not exist.")
}