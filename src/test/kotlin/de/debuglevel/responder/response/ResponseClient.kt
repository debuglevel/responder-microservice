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

    // TODO: Should probably be a reactive Flux<> instead
    @Get("/")
    fun getAll(@Header authorization: String): List<GetResponseResponse>

    @Post("/")
    fun add(@Body response: AddResponseRequest, @Header authorization: String): Mono<AddResponseResponse>

    @Put("/{id}")
    fun update(
        @NotBlank id: UUID,
        @Body response: UpdateResponseRequest,
        @Header authorization: String
    ): Mono<UpdateResponseResponse>
}