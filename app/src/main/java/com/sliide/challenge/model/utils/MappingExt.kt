package com.sliide.challenge.model.utils

import com.sliide.challenge.model.responses.User
import com.sliide.challenge.model.ui.UserUI

fun User.toUserUI(): UserUI {
    return UserUI(
        id = id,
        name = name,
        email = email,
        gender = gender,
        status = status
    )
}