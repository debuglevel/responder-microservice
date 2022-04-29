package de.debuglevel.responder.response

import java.util.*
import java.util.stream.Stream

object TestDataProvider {
    fun responseProvider() = Stream.of(
        Response(
            id = null,
            token = UUID.randomUUID().toString(),
            question = de.debuglevel.responder.question.TestDataProvider.howAreYouQuestion,
            answer = de.debuglevel.responder.answer.TestDataProvider.good,
        ),
        Response(
            id = null,
            token = UUID.randomUUID().toString(),
            question = de.debuglevel.responder.question.TestDataProvider.howAreYouQuestion,
            answer = de.debuglevel.responder.answer.TestDataProvider.neutral,
        ),
        Response(
            id = null,
            token = UUID.randomUUID().toString(),
            question = de.debuglevel.responder.question.TestDataProvider.howAreYouQuestion,
            answer = de.debuglevel.responder.answer.TestDataProvider.bad,
        ),
        Response(
            id = null,
            token = UUID.randomUUID().toString(),
            question = de.debuglevel.responder.question.TestDataProvider.participateQuestion,
            answer = de.debuglevel.responder.answer.TestDataProvider.yes,
        ),
        Response(
            id = null,
            token = UUID.randomUUID().toString(),
            question = de.debuglevel.responder.question.TestDataProvider.participateQuestion,
            answer = de.debuglevel.responder.answer.TestDataProvider.no,
        ),
    )
}