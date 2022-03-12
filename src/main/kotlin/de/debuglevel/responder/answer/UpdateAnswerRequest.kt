package de.debuglevel.responder.answer

data class UpdateAnswerRequest(
    val title: String,
) {
    fun toAnswer(): Answer {
        return Answer(
            id = null,
            title = this.title,
        )
    }
}