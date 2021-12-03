package com.sliide.challenge.ext

inline fun <reified R> Any.takeIfType(predicate: (R) -> Boolean = { true }): R? {
    val data = this as? R
    return data?.takeIf(predicate)
}