package de.debuglevel.responder.answer

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface AnswerRepository : CrudRepository<Answer, UUID>