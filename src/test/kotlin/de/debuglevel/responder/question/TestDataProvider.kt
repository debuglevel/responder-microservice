package de.debuglevel.responder.question

import java.util.stream.Stream

object TestDataProvider {
    fun questionProvider() = Stream.of(
        Question(
            id = null,
            title = "How are you?"
        ),
        Question(
            id = null,
            title = "Do you want to participate?"
        ),
    )
}