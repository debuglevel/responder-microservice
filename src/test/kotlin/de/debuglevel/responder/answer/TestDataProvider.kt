package de.debuglevel.responder.answer

import java.util.stream.Stream

object TestDataProvider {
    val good: Answer
        get() = Answer(
            id = null,
            title = "Good",
            question = de.debuglevel.responder.question.TestDataProvider.howAreYouQuestion
        )
    val neutral: Answer
        get() = Answer(
            id = null,
            title = "Neutral",
            question = de.debuglevel.responder.question.TestDataProvider.howAreYouQuestion
        )
    val bad: Answer
        get() = Answer(
            id = null,
            title = "Bad",
            question = de.debuglevel.responder.question.TestDataProvider.howAreYouQuestion
        )
    val yes: Answer
        get() = Answer(
            id = null,
            title = "Yes",
            question = de.debuglevel.responder.question.TestDataProvider.participateQuestion
        )
    val no: Answer
        get() = Answer(
            id = null,
            title = "No",
            question = de.debuglevel.responder.question.TestDataProvider.participateQuestion
        )

    fun answerProvider() =
        Stream.of(
            good,
            neutral,
            bad,
            yes,
            no,
        )
    }
