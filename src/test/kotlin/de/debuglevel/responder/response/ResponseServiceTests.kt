package de.debuglevel.responder.response

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResponseServiceTests {

    @Inject
    lateinit var responseService: ResponseService

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `save response`(response: Response) {
        // Arrange

        // Act
        val savedResponse = responseService.add(response)

        // Assert
        assertThat(savedResponse).isEqualTo(response)
    }

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `retrieve response`(response: Response) {
        // Arrange
        val savedResponse = responseService.add(response)

        // Act
        val retrievedResponse = responseService.get(savedResponse.id!!)

        // Assert
        assertThat(retrievedResponse).isEqualTo(savedResponse)
    }

    @Test
    fun `update response`() {
        // Arrange
        val response = Response(null, "Test")
        val savedResponse = responseService.add(response)

        // Act
        val retrievedResponse = responseService.get(savedResponse.id!!)
        retrievedResponse.name = "Test updated"
        val updatedResponse = responseService.update(retrievedResponse.id!!, retrievedResponse)

        // Assert
        assertThat(updatedResponse.name).isEqualTo("Test updated")
    }

    /**
     * Test updating a copy of the original entity, because this way it's ensured that the service can handle detached entities.
     */
    @Test
    fun `update response with copy()`() {
        // Arrange
        val response = Response(null, "Test")
        val savedResponse = responseService.add(response)

        // Act
        val retrievedResponse = responseService.get(savedResponse.id!!)
        val updateResponse = retrievedResponse.copy(name = "Test updated")
        val updatedResponse = responseService.update(updateResponse.id!!, updateResponse)

        // Assert
        assertThat(updatedResponse.name).isEqualTo("Test updated")
    }

    fun responseProvider() = TestDataProvider.responseProvider()
}