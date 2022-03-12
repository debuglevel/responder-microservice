package de.debuglevel.responder.question

data class AddQuestionRequest(
    val title: String,
) {
    constructor(question: Question) : this(
        title = question.title
    )

    fun toQuestion(): Question {
        return Question(
            id = null,
            title = this.title,
        )
    }
}