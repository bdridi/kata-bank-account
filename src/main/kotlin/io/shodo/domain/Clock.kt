package io.shodo.domain

import java.time.LocalDateTime

open class Clock {
    open fun getLocalDateTime(): LocalDateTime = LocalDateTime.now()
}