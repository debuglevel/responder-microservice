package de.debuglevel.responder.question

import java.util.*

data class GetQuestionResponse(
    val id: UUID,
    val title: String,
) {
    constructor(question: Question) : this(
        question.id!!,
        question.title,
    )
}