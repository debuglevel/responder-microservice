package de.debuglevel.responder.answer

import de.debuglevel.responder.question.Question
import de.debuglevel.responder.response.Response
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
data class Answer(
    /**
     * @implNote: Needs @GeneratedValue(generator = "uuid2"), @GenericGenerator and @Column to work with MariaDB/MySQL. See https://github.com/micronaut-projects/micronaut-data/issues/1210
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    var id: UUID?,
    var title: String,
    @ManyToOne
    var question: Question,
    @OneToMany(cascade = [CascadeType.ALL])
    val responses: Set<Response> = setOf(),
    @DateCreated
    var createdOn: LocalDateTime = LocalDateTime.now(),
    @DateUpdated
    var lastModifiedOn: LocalDateTime = LocalDateTime.now(),
)