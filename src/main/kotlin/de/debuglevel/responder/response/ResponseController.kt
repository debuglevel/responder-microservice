package de.debuglevel.responder.response

import de.debuglevel.responder.answer.AnswerService
import de.debuglevel.responder.question.QuestionService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import java.net.URI
import java.util.*

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/responses")
@Tag(name = "responses")
class ResponseController(
    private val responseService: ResponseService,
    private val questionService: QuestionService,
    private val answerService: AnswerService,
) {
    private val logger = KotlinLogging.logger {}

    /**
     * Get all responses
     * @return All responses
     */
    @Get("/")
    fun getAllResponses(): HttpResponse<List<GetResponseResponse>> {
        logger.debug("Called getAllResponses()")
        return try {
            val responses = responseService.getAll()
            val getResponseResponses = responses
                .map { GetResponseResponse(it) }

            HttpResponse.ok(getResponseResponses)
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Get a response
     * @param id ID of the response
     * @return A response
     */
    @Get("/{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun getOneResponse(id: UUID): HttpResponse<GetResponseResponse> {
        logger.debug("Called getOneResponse($id)")
        return try {
            val response = responseService.get(id)

            val getResponseResponse = GetResponseResponse(response)
            HttpResponse.ok(getResponseResponse)
        } catch (e: ResponseService.ItemNotFoundException) {
            logger.debug { "Getting response $id failed: ${e.message}" }
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Create a response.
     * @return A response with their ID
     */
    @Post("/")
    fun postOneResponse(addResponseRequest: AddResponseRequest): HttpResponse<AddResponseResponse> {
        logger.debug("Called postOneResponse($addResponseRequest)")

        return try {
            val response = addResponseRequest.toResponse(questionService)
            val addedResponse = responseService.add(response)

            val addResponseResponse = AddResponseResponse(addedResponse)
            HttpResponse.created(addResponseResponse, URI(addedResponse.id.toString()))
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Update a response.
     * @param id ID of the response
     * @return The updated response
     */
    @Put("/{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun putOneResponse(id: UUID, updateResponseRequest: UpdateResponseRequest): HttpResponse<UpdateResponseResponse> {
        logger.debug("Called putOneResponse($id, $updateResponseRequest)")

        return try {
            val answer = answerService.get(updateResponseRequest.answerId)
            val updatedResponse = responseService.submitAnswer(id, answer)

            val updateResponseResponse = UpdateResponseResponse(updatedResponse)
            HttpResponse.ok(updateResponseResponse)
        } catch (e: ResponseService.ItemNotFoundException) {
            logger.debug { "Updating response $id failed: ${e.message}" }
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Delete a response.
     * @param id ID of the response
     */
    @Delete("/{id}")
    fun deleteOneResponse(id: UUID): HttpResponse<Unit> {
        logger.debug("Called deleteOneResponse($id)")
        return try {
            responseService.delete(id)

            HttpResponse.noContent()
        } catch (e: ResponseService.ItemNotFoundException) {
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Delete all responses.
     */
    @Delete("/")
    fun deleteAllResponses(): HttpResponse<Unit> {
        logger.debug("Called deleteAllResponses()")
        return try {
            responseService.deleteAll()

            HttpResponse.noContent()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }
}