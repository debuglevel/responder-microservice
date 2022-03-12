//package de.debuglevel.responder.response
//
//import de.debuglevel.responder.question.QuestionService
//import java.util.*
//
//data class AddResponseRequest(
//    val token: String,
//    val questionId: UUID,
//) {
//    constructor(response: Response) : this(
//        token = response.token,
//        questionId = response.question.id!!,
//    )
//
//    fun toResponse(questionService: QuestionService): Response {
//        return Response(
//            id = null,
//            token = this.token,
//            question = questionService.get(this.questionId),
//        )
//    }
//}