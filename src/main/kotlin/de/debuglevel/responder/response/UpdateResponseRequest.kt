package de.debuglevel.responder.response

data class UpdateResponseRequest(
    val token: String,
) {
    fun toResponse(): Response {
        return Response(
            id = null,
            token = this.token,
        )
    }
}