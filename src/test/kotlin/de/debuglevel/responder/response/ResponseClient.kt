package de.debuglevel.responder.response

import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.constraints.NotBlank

@Client("/responses")
interface ResponseClient {
    @Get("/{id}")
    fun get(@NotBlank id: UUID): Mono<GetResponseResponse>

    @Post("/")
    fun add(@Body response: AddResponseRequest): Mono<AddResponseResponse>

    @Put("/{id}")
    fun update(@NotBlank id: UUID, @Body response: UpdateResponseRequest): Mono<UpdateResponseResponse>

    @Get("/")
    fun list(): List<GetResponseResponse>

    @Get("/VIPs")
    fun getVIPs(@Header authorization: String): Set<GetResponseResponse>
}