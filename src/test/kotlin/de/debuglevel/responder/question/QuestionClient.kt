package de.debuglevel.responder.question

import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.constraints.NotBlank

@Client("/questions")
interface QuestionClient {
    @Get("/{id}")
    fun get(@NotBlank id: UUID): Mono<GetQuestionResponse>

    // TODO: Should probably be a reactive Flux<> instead
    @Get("/")
    fun getAll(@Header authorization: String): List<GetQuestionResponse>

    @Post("/")
    fun add(@Body question: AddQuestionRequest, @Header authorization: String): Mono<AddQuestionResponse>

    @Put("/{id}")
    fun update(
        @NotBlank id: UUID,
        @Body question: UpdateQuestionRequest,
        @Header authorization: String
    ): Mono<UpdateQuestionResponse>
}