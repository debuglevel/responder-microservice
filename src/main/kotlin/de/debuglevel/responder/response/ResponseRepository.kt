package de.debuglevel.responder.response

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface ResponseRepository : CrudRepository<Response, UUID>