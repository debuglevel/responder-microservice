package de.debuglevel.responder.person

data class UpdatePersonRequest(
    val name: String,
) {
    fun toPerson(): Person {
        return Person(
            id = null,
            name = this.name,
        )
    }
}