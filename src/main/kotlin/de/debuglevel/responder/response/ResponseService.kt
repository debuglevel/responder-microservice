package de.debuglevel.responder.response

import de.debuglevel.responder.answer.Answer
import jakarta.inject.Singleton
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.*

@Singleton
class ResponseService(
    private val responseRepository: ResponseRepository,
) {
    private val logger = KotlinLogging.logger {}

    val count: Long
        get() {
            logger.debug { "Getting responses count..." }

            val count = responseRepository.count()

            logger.debug { "Got responses count: $count" }
            return count
        }

    fun exists(id: UUID): Boolean {
        logger.debug { "Checking if response $id exists..." }

        val isExisting = responseRepository.existsById(id)

        logger.debug { "Checked if response $id exists: $isExisting" }
        return isExisting
    }

    fun get(id: UUID): Response {
        logger.debug { "Getting response with ID '$id'..." }

        val response: Response = responseRepository.findById(id).orElseThrow {
            logger.debug { "Getting response with ID '$id' failed" }
            ItemNotFoundException(id)
        }

        logger.debug { "Got response with ID '$id': $response" }
        return response
    }

    fun getAll(): Set<Response> {
        logger.debug { "Getting all responses..." }

        val responses = responseRepository.findAll().toSet()

        logger.debug { "Got ${responses.size} responses" }
        return responses
    }

    fun add(response: Response): Response {
        logger.debug { "Adding response '$response'..." }

        val savedResponse = responseRepository.save(response)

        logger.debug { "Added response: $savedResponse" }
        return savedResponse
    }

//    fun update(id: UUID, response: Response): Response {
//        logger.debug { "Updating response '$response' with ID '$id'..." }
//
//        // an object must be known to Hibernate (i.e. retrieved first) to get updated;
//        // it would be a "detached entity" otherwise.
//        val updateResponse = this.get(id).apply {
//            token = response.token
//            answer = response.answer
//            answeredOn = response.answeredOn
//        }
//
//        val updatedResponse = responseRepository.update(updateResponse)
//
//        logger.debug { "Updated response: $updatedResponse with ID '$id'" }
//        return updatedResponse
//    }

    fun submitAnswer(id: UUID, newAnswer: Answer): Response {
        logger.debug { "Submitting answer $newAnswer to response with ID '$id'..." }

        val response = this.get(id)

        if (!response.question.answers.map { it.id }.contains(newAnswer.id))
        {
            logger.warn { "Answer $newAnswer does not belong to Question ${response.question}" }
            throw IllegalAnswerException(newAnswer)
        }

        val answeredOn_ = response.answeredOn
        if (answeredOn_ != null)
        {
            logger.debug { "Already answered on ${response.answeredOn}" }
            throw AlreadyRespondedException(answeredOn_)
        }

        // An object must be known to Hibernate (i.e. retrieved first) to get updated;
        // it would be a "detached entity" otherwise.
        val updateResponse = this.get(id).apply {
            answer = newAnswer
            answeredOn = LocalDateTime.now()
        }

        val updatedResponse = responseRepository.update(updateResponse)

        logger.debug { "Submitted answer $newAnswer to response $updatedResponse with ID '$id'" }
        return updatedResponse
    }

    fun delete(id: UUID) {
        logger.debug { "Deleting response with ID '$id'..." }

        if (responseRepository.existsById(id)) {
            responseRepository.deleteById(id)
        } else {
            throw ItemNotFoundException(id)
        }

        logger.debug { "Deleted response with ID '$id'" }
    }

    fun deleteAll() {
        logger.debug { "Deleting all responses..." }

        val countBefore = responseRepository.count()
        responseRepository.deleteAll() // CAVEAT: does not delete dependent entities; use this instead: responseRepository.findAll().forEach { responseRepository.delete(it) }
        val countAfter = responseRepository.count()
        val countDeleted = countBefore - countAfter

        logger.debug { "Deleted $countDeleted of $countBefore responses, $countAfter remaining" }
    }

    class ItemNotFoundException(criteria: Any) : Exception("Item '$criteria' does not exist.")
    class IllegalAnswerException(answer: Answer) : Exception("Answer '$answer' does not belong to Question.")
    class AlreadyRespondedException(answeredOn: LocalDateTime) : Exception("Already answered on '$answeredOn'.")
}