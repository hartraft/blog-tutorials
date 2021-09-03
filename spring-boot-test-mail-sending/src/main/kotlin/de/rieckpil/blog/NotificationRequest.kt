package de.rieckpil.blog

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class NotificationRequest(
    var email: @Email String? = null,
    var content: @NotBlank String? = null
)