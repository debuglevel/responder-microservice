package de.debuglevel.responder.response

import com.google.rpc.context.AttributeContext
import de.debuglevel.responder.answer.Answer
import de.debuglevel.responder.question.Question
import java.util.*

data class AddResponseResponse(
    val id: UUID,
    val token: String,
    val questionId: UUID,
) {
    constructor(response: Response) : this(
        response.id!!,
        response.token,
        response.question.id!!,
    )

    fun toResponse(question: Question): Response {
        return Response(
            id = this.id,
            token = this.token,
            question = question,
        )
    }
}