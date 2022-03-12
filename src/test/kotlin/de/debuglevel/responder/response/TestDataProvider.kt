package de.debuglevel.responder.response

import java.util.stream.Stream

object TestDataProvider {
    fun responseProvider() = Stream.of(
        Response(
            id = null,
            name = "Mozart"
        ),
        Response(
            id = null,
            name = "Hänschen"
        ),
        Response(
            id = null,
            name = "コハウプト マルク"
        ),
        Response(
            id = null,
            name = "Max Mustermann"
        )
    )
}