//package de.debuglevel.responder.answer
//
//import de.debuglevel.responder.question.QuestionService
//import java.util.*
//
//data class AddAnswerRequest(
//    val title: String,
//    val questionId: UUID,
//) {
//    constructor(answer: Answer) : this(
//        title = answer.title,
//        questionId = answer.question.id!!,
//    )
//
//    fun toAnswer(questionService: QuestionService): Answer {
//        return Answer(
//            id = null,
//            title = this.title,
//            question = questionService.get(this.questionId),
//        )
//    }
//}