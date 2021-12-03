package com.sliide.challenge.model.requests

import com.sliide.challenge.model.responses.Gender
import com.sliide.challenge.model.responses.Status

data class UserBody(
    val name: String,
    val email: String,
    val gender: Gender,
    val status: Status
)