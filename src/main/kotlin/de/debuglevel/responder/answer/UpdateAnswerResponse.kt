package de.debuglevel.responder.answer

import java.util.*

data class UpdateAnswerResponse(
    val id: UUID,
    val title: String,
    val questionId: UUID,
) {
    constructor(answer: Answer) : this(
        answer.id!!,
        answer.title,
        answer.question.id!!,
    )
}