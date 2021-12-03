package com.sliide.challenge.model.responses

data class BaseResponse<META, MODEL>(
    val meta: META?,
    val data: MODEL?
)