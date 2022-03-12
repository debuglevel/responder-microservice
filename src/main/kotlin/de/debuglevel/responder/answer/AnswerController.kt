package de.debuglevel.responder.answer

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import java.net.URI
import java.util.*

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/answers")
@Tag(name = "answers")
class AnswerController(private val answerService: AnswerService) {
    private val logger = KotlinLogging.logger {}

    /**
     * Get all answers
     * @return All answers
     */
    @Get("/")
    fun getAllAnswers(): HttpResponse<List<GetAnswerResponse>> {
        logger.debug("Called getAllAnswers()")
        return try {
            val answers = answerService.getAll()
            val getAnswerResponses = answers
                .map { GetAnswerResponse(it) }

            HttpResponse.ok(getAnswerResponses)
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Get a answer
     * @param id ID of the answer
     * @return A answer
     */
    @Get("/{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun getOneAnswer(id: UUID): HttpResponse<GetAnswerResponse> {
        logger.debug("Called getOneAnswer($id)")
        return try {
            val answer = answerService.get(id)

            val getAnswerResponse = GetAnswerResponse(answer)
            HttpResponse.ok(getAnswerResponse)
        } catch (e: AnswerService.ItemNotFoundException) {
            logger.debug { "Getting answer $id failed: ${e.message}" }
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Create a answer.
     * @return A answer with their ID
     */
    @Post("/")
    fun postOneAnswer(addAnswerRequest: AddAnswerRequest): HttpResponse<AddAnswerResponse> {
        logger.debug("Called postOneAnswer($addAnswerRequest)")

        return try {
            val answer = addAnswerRequest.toAnswer()
            val addedAnswer = answerService.add(answer)

            val addAnswerResponse = AddAnswerResponse(addedAnswer)
            HttpResponse.created(addAnswerResponse, URI(addedAnswer.id.toString()))
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Update a answer.
     * @param id ID of the answer
     * @return The updated answer
     */
    @Put("/{id}")
    fun putOneAnswer(id: UUID, updateAnswerRequest: UpdateAnswerRequest): HttpResponse<UpdateAnswerResponse> {
        logger.debug("Called putOneAnswer($id, $updateAnswerRequest)")

        return try {
            val answer = updateAnswerRequest.toAnswer()
            val updatedAnswer = answerService.update(id, answer)

            val updateAnswerResponse = UpdateAnswerResponse(updatedAnswer)
            HttpResponse.ok(updateAnswerResponse)
        } catch (e: AnswerService.ItemNotFoundException) {
            logger.debug { "Updating answer $id failed: ${e.message}" }
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Delete a answer.
     * @param id ID of the answer
     */
    @Delete("/{id}")
    fun deleteOneAnswer(id: UUID): HttpResponse<Unit> {
        logger.debug("Called deleteOneAnswer($id)")
        return try {
            answerService.delete(id)

            HttpResponse.noContent()
        } catch (e: AnswerService.ItemNotFoundException) {
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Delete all answer.
     */
    @Delete("/")
    fun deleteAllAnswers(): HttpResponse<Unit> {
        logger.debug("Called deleteAllAnswers()")
        return try {
            answerService.deleteAll()

            HttpResponse.noContent()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }
}