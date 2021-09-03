package de.rieckpil.blog

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetupTest
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class NotificationControllerIT {
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
        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAsserted {
            val receivedMessages = greenMail.receivedMessages
            Assertions.assertEquals(1, receivedMessages.size)
            val receivedMessage = receivedMessages[0]
            Assertions.assertEquals("Hello World!", GreenMailUtil.getBody(receivedMessage))
            Assertions.assertEquals(1, receivedMessage.allRecipients.size)
            Assertions.assertEquals("duke@spring.io", receivedMessage.allRecipients[0].toString())
        }
    }

    companion object {
        @RegisterExtension
        @JvmField
        val greenMail: GreenMailExtension = GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
            .withPerMethodLifecycle(false)
    }

}