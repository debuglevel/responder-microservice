package de.debuglevel.responder.question

data class UpdateQuestionRequest(
    val title: String,
) {
    fun toQuestion(): Question {
        return Question(
            id = null,
            title = this.title,
        )
    }
}