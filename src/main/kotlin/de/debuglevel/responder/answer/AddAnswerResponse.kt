package de.debuglevel.responder.answer

import de.debuglevel.responder.question.Question
import java.util.*

data class AddAnswerResponse(
    val id: UUID,
    val title: String,
    val questionId: UUID,
) {
    constructor(answer: Answer) : this(
        answer.id!!,
        answer.title,
        answer.question.id!!,
    )

    fun toAnswer(question: Question): Answer {
        return Answer(
            id = this.id,
            title = this.title,
            question = question,
        )
    }
}