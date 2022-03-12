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

    @Post("/")
    fun add(@Body question: AddQuestionRequest): Mono<AddQuestionResponse>

    @Put("/{id}")
    fun update(@NotBlank id: UUID, @Body question: UpdateQuestionRequest): Mono<UpdateQuestionResponse>

    @Get("/")
    fun list(): List<GetQuestionResponse>

    @Get("/VIPs")
    fun getVIPs(@Header authorization: String): Set<GetQuestionResponse>
}