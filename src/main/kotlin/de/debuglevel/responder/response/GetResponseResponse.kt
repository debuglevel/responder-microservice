//package de.debuglevel.responder.response
//
//import java.time.LocalDateTime
//import java.util.*
//
//data class GetResponseResponse(
//    val id: UUID,
//    val token: String,
//    val questionId: UUID,
//    val answerId: UUID?,
//    val answeredOn: LocalDateTime?,
//    val createdOn: LocalDateTime,
//    val lastModifiedOn: LocalDateTime,
//) {
//    constructor(response: Response) : this(
//        response.id!!,
//        response.token,
//        response.question.id!!,
//        response.answer?.id,
//        response.answeredOn,
//        response.createdOn,
//        response.lastModifiedOn,
//    )
//}