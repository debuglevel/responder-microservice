package de.debuglevel.responder.answer

import java.util.stream.Stream

object TestDataProvider {
    fun answerProvider() = Stream.of(
        Answer(
            id = null,
            name = "Mozart"
        ),
        Answer(
            id = null,
            name = "Hänschen"
        ),
        Answer(
            id = null,
            name = "コハウプト マルク"
        ),
        Answer(
            id = null,
            name = "Max Mustermann"
        )
    )
}