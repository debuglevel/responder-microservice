package de.debuglevel.responder.answer

data class UpdateAnswerRequest(
    val title: String,
) {
//    fun toAnswer(questionService: QuestionService): Answer {
//        return Answer(
//            id = null,
//            title = this.title,
//            question = questionService.get(this.questionId),
//        )
//    }
}