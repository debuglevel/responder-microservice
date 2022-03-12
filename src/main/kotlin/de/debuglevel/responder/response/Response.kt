//package de.debuglevel.responder.response
//
//import de.debuglevel.responder.answer.Answer
//import de.debuglevel.responder.question.Question
//import io.micronaut.data.annotation.DateCreated
//import io.micronaut.data.annotation.DateUpdated
//import org.hibernate.annotations.GenericGenerator
//import java.time.LocalDateTime
//import java.util.*
//import javax.persistence.*
//
//@Entity
//data class Response(
//    /**
//     * @implNote: Needs @GeneratedValue(generator = "uuid2"), @GenericGenerator and @Column to work with MariaDB/MySQL. See https://github.com/micronaut-projects/micronaut-data/issues/1210
//     */
//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Column(columnDefinition = "BINARY(16)")
//    var id: UUID?,
//    var token: String,
//    @ManyToOne
//    var question: Question,
//    @ManyToOne
//    var answer: Answer? = null,
//    var answeredOn: LocalDateTime? = null,
//    @DateCreated
//    var createdOn: LocalDateTime = LocalDateTime.now(),
//    @DateUpdated
//    var lastModifiedOn: LocalDateTime = LocalDateTime.now(),
//) {
//    override fun toString(): String {
//        return "Response(id=$id, token='$token', answeredOn=$answeredOn)"
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as Response
//
//        if (token != other.token) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        return token.hashCode()
//    }
//}