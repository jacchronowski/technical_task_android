package com.sliide.challenge.model.ui

import com.sliide.challenge.model.responses.Gender
import com.sliide.challenge.model.responses.Status
import com.sliide.challenge.ext.getTimeAgo
import java.util.*

data class UserUI(
    val id: Long,
    val name: String,
    val email: String,
    val gender: Gender,
    val status: Status,
    val creationDate: Date = Date(),
    val timeAgo: String? = creationDate.getTimeAgo(),
    val isLoading: Boolean = false
)