package com.simovic.simplegaming.base.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Long.toLocalDateString(): String {
    val instant = Instant.ofEpochMilli(this)
    val zone = ZoneId.systemDefault()
    val local = LocalDateTime.ofInstant(instant, zone).toLocalDate()
    return local.toString() // 2025-06-12
}

fun Long.toLocalDate(): LocalDate? =
    Instant
        .ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

private val birthdayFormatter = DateTimeFormatter.ofPattern("dd.MM")

fun LocalDate.toBirthDay(): String = this.format(birthdayFormatter)
