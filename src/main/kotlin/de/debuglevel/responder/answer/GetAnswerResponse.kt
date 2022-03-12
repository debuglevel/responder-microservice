package de.debuglevel.responder.answer

import java.util.*

data class GetAnswerResponse(
    val id: UUID,
    val title: String,
) {
    constructor(answer: Answer) : this(
        answer.id!!,
        answer.title,
    )
}