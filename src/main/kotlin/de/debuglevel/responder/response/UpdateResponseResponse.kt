//package de.debuglevel.responder.response
//
//import java.util.*
//
//data class UpdateResponseResponse(
//    val id: UUID,
//    val token: String,
//    val answerId: UUID,
//) {
//    constructor(response: Response) : this(
//        response.id!!,
//        response.token,
//        response.answer!!.id!!,
//    )
//}