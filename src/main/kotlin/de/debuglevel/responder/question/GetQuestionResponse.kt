package de.debuglevel.responder.question

import java.time.LocalDateTime
import java.util.*

data class GetQuestionResponse(
    val id: UUID,
    val title: String,
//    val answerIds: List<UUID>,
    val createdOn: LocalDateTime,
    val lastModifiedOn: LocalDateTime,
) {
    constructor(question: Question) : this(
        question.id!!,
        question.title,
//        question.answers.map { it.id!! },
        question.createdOn,
        question.lastModifiedOn,
    )
}