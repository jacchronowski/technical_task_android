package com.sliide.challenge.model.responses

import com.squareup.moshi.Json

enum class Status {
    @Json(name = "active")
    ACTIVE,
    @Json(name = "inactive")
    INACTIVE
}
