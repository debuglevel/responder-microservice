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

    @Post("/")
    fun add(@Body answer: AddAnswerRequest): Mono<AddAnswerResponse>

    @Put("/{id}")
    fun update(@NotBlank id: UUID, @Body answer: UpdateAnswerRequest): Mono<UpdateAnswerResponse>

    @Get("/")
    fun list(): List<GetAnswerResponse>

    @Get("/VIPs")
    fun getVIPs(@Header authorization: String): Set<GetAnswerResponse>
}