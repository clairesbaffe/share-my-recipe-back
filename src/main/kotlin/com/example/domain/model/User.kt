package com.example.domain.model

import java.time.LocalDate

data class User(
    val id: Long,
    val username: String,
    val passwordHash: String,
    val roles: List<String>,
    val date: LocalDate
)