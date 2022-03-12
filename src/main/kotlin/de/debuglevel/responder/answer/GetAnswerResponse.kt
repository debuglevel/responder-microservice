package de.debuglevel.responder.answer

import java.time.LocalDateTime
import java.util.*

data class GetAnswerResponse(
    val id: UUID,
    val title: String,
    val questionId: UUID,
    val createdOn: LocalDateTime,
    val lastModifiedOn: LocalDateTime,
) {
    constructor(answer: Answer) : this(
        answer.id!!,
        answer.title,
        answer.question.id!!,
        answer.createdOn,
        answer.lastModifiedOn,
    )
}