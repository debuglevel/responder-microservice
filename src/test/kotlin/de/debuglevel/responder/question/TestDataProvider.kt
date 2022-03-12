package de.debuglevel.responder.question

import java.util.stream.Stream

object TestDataProvider {
    fun questionProvider() = Stream.of(
        Question(
            id = null,
            name = "Mozart"
        ),
        Question(
            id = null,
            name = "Hänschen"
        ),
        Question(
            id = null,
            name = "コハウプト マルク"
        ),
        Question(
            id = null,
            name = "Max Mustermann"
        )
    )
}