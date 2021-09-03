package de.rieckpil.blog

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class NotificationControllerSecondIT {
    @Autowired
    private val testRestTemplate: TestRestTemplate? = null

    @Test
    @Throws(Exception::class)
    fun shouldSendEmailWithCorrectPayloadToUser() {
        val payload = "{ \"email\": \"duke@spring.io\", \"content\": \"Hello World!\"}"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(payload, headers)
        val response = testRestTemplate!!.postForEntity("/notifications", request, Void::class.java)
        Assertions.assertEquals(200, response.statusCodeValue)
    }

    companion object {
        @Container
        @JvmStatic
        var greenMailContainer: GenericContainer<*> =
            GenericContainer<Nothing>(DockerImageName.parse("greenmail/standalone:1.6.1")).apply {
                withExposedPorts(3025)
                withEnv(
                    "GREENMAIL_OPTS",
                    "-Dgreenmail.setup.test.all -Dgreenmail.hostname=0.0.0.0 -Dgreenmail.users=duke:springboot"
                )
            }

        @DynamicPropertySource
        @JvmStatic
        fun configureMailHost(registry: DynamicPropertyRegistry) {
            registry.add("spring.mail.host") { greenMailContainer.host }
            registry.add("spring.mail.port") { greenMailContainer.firstMappedPort }
        }
    }
}