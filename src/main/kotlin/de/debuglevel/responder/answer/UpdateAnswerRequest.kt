//package de.debuglevel.responder.answer
//
//import de.debuglevel.responder.question.QuestionService
//import java.util.*
//
//data class UpdateAnswerRequest(
//    val title: String,
//    val questionId: UUID,
//) {
//    fun toAnswer(questionService: QuestionService): Answer {
//        return Answer(
//            id = null,
//            title = this.title,
//            question = questionService.get(this.questionId),
//        )
//    }
//}