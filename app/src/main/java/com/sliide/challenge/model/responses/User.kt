package com.sliide.challenge.model.responses

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val gender: Gender,
    val status: Status
)

