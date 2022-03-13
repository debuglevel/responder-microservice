package de.debuglevel.responder.answer

import java.util.stream.Stream

object TestDataProvider {
    fun answerProvider() = Stream.of(
        Answer(
            id = null,
            title = "Good",
            question = de.debuglevel.responder.question.TestDataProvider.howAreYouQuestion
        ),
        Answer(
            id = null,
            title = "Neutral",
            question = de.debuglevel.responder.question.TestDataProvider.howAreYouQuestion
        ),
        Answer(
            id = null,
            title = "Bad",
            question = de.debuglevel.responder.question.TestDataProvider.howAreYouQuestion
        ),
        Answer(
            id = null,
            title = "Yes",
            question = de.debuglevel.responder.question.TestDataProvider.participateQuestion
        ),
        Answer(
            id = null,
            title = "No",
            question = de.debuglevel.responder.question.TestDataProvider.participateQuestion
        ),
    )
}