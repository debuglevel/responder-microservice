package de.debuglevel.responder.response

import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResponseControllerTests {
    @Inject
    lateinit var responseClient: ResponseClient

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `add response`(response: Response) {
        // Arrange
        val addResponseRequest = AddResponseRequest(response)

        // Act
        val addedResponse = responseClient.add(addResponseRequest).block()

        // Assert
        Assertions.assertThat(addedResponse.name).isEqualTo(response.name)
        Assertions.assertThat(addedResponse.name).isEqualTo(addResponseRequest.name)
    }

    @ParameterizedTest
    @MethodSource("responseProvider")
    fun `get response`(response: Response) {
        // Arrange
        val addResponseRequest = AddResponseRequest(response)
        val addedResponse = responseClient.add(addResponseRequest).block()

        // Act
        val getResponse = responseClient.get(addedResponse.id).block()

        // Assert
        Assertions.assertThat(getResponse.id).isEqualTo(addedResponse.id)
        Assertions.assertThat(getResponse.name).isEqualTo(response.name)
        Assertions.assertThat(getResponse.name).isEqualTo(addedResponse.name)
    }

    @Test
    fun `get non-existing response`() {
        // Arrange

        // Act
        val getResponseResponse = responseClient.get(UUID.randomUUID()).block()

        // Assert
        Assertions.assertThat(getResponseResponse).isNull()
    }

    @Test
    fun `update response`() {
        // Arrange
        val addResponseRequest = AddResponseRequest("Original Name")
        val addedResponse = responseClient.add(addResponseRequest).block()
        val updateResponseRequest = UpdateResponseRequest("Updated Name")

        // Act
        val updatedResponse = responseClient.update(addedResponse.id, updateResponseRequest).block()
        val getResponse = responseClient.get(addedResponse.id).block()

        // Assert
        Assertions.assertThat(updatedResponse.id).isEqualTo(addedResponse.id)
        Assertions.assertThat(getResponse.id).isEqualTo(addedResponse.id)
        Assertions.assertThat(updatedResponse.name).isEqualTo(updateResponseRequest.name)
    }

    @Test
    fun `update non-existing response`() {
        // Arrange
        val updateResponseRequest = UpdateResponseRequest("Updated Name")

        // Act
        val getResponseResponse = responseClient.update(UUID.randomUUID(), updateResponseRequest).block()

        // Assert
        Assertions.assertThat(getResponseResponse).isNull()
    }

    @Test
    fun `list responses`() {
        // Arrange
        responseProvider().forEach {
            responseClient.add(AddResponseRequest(it)).block()
        }

        // Act
        val getResponses = responseClient.list()

        // Assert
        Assertions.assertThat(getResponses).extracting<String> { x -> x.name }
            .containsAll(responseProvider().map { it.name }.toList())
    }

    @Test
    fun `get VIPs`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:SECRET_PASSWORD".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val getResponses = responseClient.getVIPs(basicAuthenticationHeader)

        // Assert
        Assertions.assertThat(getResponses).anyMatch { it.name == "Hermione Granger" }
        Assertions.assertThat(getResponses).anyMatch { it.name == "Harry Potter" }
        Assertions.assertThat(getResponses).anyMatch { it.name == "Ronald Weasley" }
    }

    @Test
    fun `fail get VIPs with bad authentication`() {
        // Arrange

        // Act
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:wrongPassword".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        val thrown = Assertions.catchThrowable {
            responseClient.getVIPs(basicAuthenticationHeader)
        }

        // Assert
        Assertions.assertThat(thrown)
            .isInstanceOf(HttpClientResponseException::class.java)
            .hasMessageContaining("Unauthorized")
    }

    fun responseProvider() = TestDataProvider.responseProvider()
}