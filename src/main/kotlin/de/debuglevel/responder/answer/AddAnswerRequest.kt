package de.debuglevel.responder.answer

data class AddAnswerRequest(
    val title: String,
) {
    constructor(answer: Answer) : this(
        title = answer.title
    )

    fun toAnswer(): Answer {
        return Answer(
            id = null,
            title = this.title,
        )
    }
}