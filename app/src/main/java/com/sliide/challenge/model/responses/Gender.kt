package com.sliide.challenge.model.responses

import com.squareup.moshi.Json

enum class Gender {
    @Json(name = "male")
    MALE,
    @Json(name = "female")
    FEMALE
}