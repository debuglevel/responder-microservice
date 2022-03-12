package de.debuglevel.responder.question

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import java.net.URI
import java.util.*

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/questions")
@Tag(name = "questions")
class QuestionController(private val questionService: QuestionService) {
    private val logger = KotlinLogging.logger {}

    /**
     * Get all questions
     * @return All questions
     */
    @Get("/")
    fun getAllQuestions(): HttpResponse<List<GetQuestionResponse>> {
        logger.debug("Called getAllQuestions()")
        return try {
            val questions = questionService.getAll()
            val getQuestionResponses = questions
                .map { GetQuestionResponse(it) }

            HttpResponse.ok(getQuestionResponses)
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Get a question
     * @param id ID of the question
     * @return A question
     */
    @Get("/{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun getOneQuestion(id: UUID): HttpResponse<GetQuestionResponse> {
        logger.debug("Called getOneQuestion($id)")
        return try {
            val question = questionService.get(id)

            val getQuestionResponse = GetQuestionResponse(question)
            HttpResponse.ok(getQuestionResponse)
        } catch (e: QuestionService.ItemNotFoundException) {
            logger.debug { "Getting question $id failed: ${e.message}" }
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Create a question.
     * @return A question with their ID
     */
    @Post("/")
    fun postOneQuestion(addQuestionRequest: AddQuestionRequest): HttpResponse<AddQuestionResponse> {
        logger.debug("Called postOneQuestion($addQuestionRequest)")

        return try {
            val question = addQuestionRequest.toQuestion()
            val addedQuestion = questionService.add(question)

            val addQuestionResponse = AddQuestionResponse(addedQuestion)
            HttpResponse.created(addQuestionResponse, URI(addedQuestion.id.toString()))
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Update a question.
     * @param id ID of the question
     * @return The updated question
     */
    @Put("/{id}")
    fun putOneQuestion(id: UUID, updateQuestionRequest: UpdateQuestionRequest): HttpResponse<UpdateQuestionResponse> {
        logger.debug("Called putOneQuestion($id, $updateQuestionRequest)")

        return try {
            val question = updateQuestionRequest.toQuestion()
            val updatedQuestion = questionService.update(id, question)

            val updateQuestionResponse = UpdateQuestionResponse(updatedQuestion)
            HttpResponse.ok(updateQuestionResponse)
        } catch (e: QuestionService.ItemNotFoundException) {
            logger.debug { "Updating question $id failed: ${e.message}" }
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Delete a question.
     * @param id ID of the question
     */
    @Delete("/{id}")
    fun deleteOneQuestion(id: UUID): HttpResponse<Unit> {
        logger.debug("Called deleteOneQuestion($id)")
        return try {
            questionService.delete(id)

            HttpResponse.noContent()
        } catch (e: QuestionService.ItemNotFoundException) {
            HttpResponse.notFound()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }

    /**
     * Delete all question.
     */
    @Delete("/")
    fun deleteAllQuestions(): HttpResponse<Unit> {
        logger.debug("Called deleteAllQuestions()")
        return try {
            questionService.deleteAll()

            HttpResponse.noContent()
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError()
        }
    }
}