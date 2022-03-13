package de.debuglevel.responder.answer

import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.constraints.NotBlank

@Client("/answers")
interface AnswerClient {
    @Get("/{id}")
    fun get(@NotBlank id: UUID): Mono<GetAnswerResponse>

    // TODO: Should probably be a reactive Flux<> instead
    @Get("/")
    fun getAll(@Header authorization: String): List<GetAnswerResponse>

    @Post("/")
    fun add(@Body answer: AddAnswerRequest, @Header authorization: String): Mono<AddAnswerResponse>

    @Put("/{id}")
    fun update(
        @NotBlank id: UUID,
        @Body answer: UpdateAnswerRequest,
        @Header authorization: String
    ): Mono<UpdateAnswerResponse>
}