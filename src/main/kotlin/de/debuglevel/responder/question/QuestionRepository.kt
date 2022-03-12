package de.debuglevel.responder.question

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface QuestionRepository : CrudRepository<Question, UUID>