package de.rieckpil.blog

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/notifications")
class NotificationController(private val notificationService: NotificationService) {
    @PostMapping
    fun createNotification(@RequestBody @Valid request: NotificationRequest) {
        notificationService.notifyUser(request.email, request.content)
    }
}