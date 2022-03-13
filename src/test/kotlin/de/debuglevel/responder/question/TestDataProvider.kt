package de.debuglevel.responder.question

import java.util.stream.Stream

object TestDataProvider {
    val howAreYouQuestion: Question
        get() = Question(
            id = null,
            title = "How are you?"
        )

    val participateQuestion: Question
        get() = Question(
            id = null,
            title = "Do you want to participate?"
        )

    fun questionProvider() = Stream.of(
        howAreYouQuestion,
        participateQuestion,
    )
}
