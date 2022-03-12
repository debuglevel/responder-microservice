package de.debuglevel.responder.response

data class AddResponseRequest(
    val token: String,
) {
    constructor(response: Response) : this(
        token = response.token
    )

    fun toResponse(): Response {
        return Response(
            id = null,
            token = this.token,
        )
    }
}