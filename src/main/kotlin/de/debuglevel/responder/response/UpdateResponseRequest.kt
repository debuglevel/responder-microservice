//package de.debuglevel.responder.response
//
//import java.util.*
//
//data class UpdateResponseRequest(
//    val token: String,
//    val answerId: UUID
//) {
//    fun toResponse(): Response {
//        return Response(
//            id = null,
//            token = this.token,
//        )
//    }
//}