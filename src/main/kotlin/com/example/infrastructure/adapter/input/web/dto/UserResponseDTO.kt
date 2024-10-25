package com.example.infrastructure.adapter.input.web.dto

import java.time.LocalDate

data class UserResponseDTO(
    val id: Long,
    val username: String,
    val creationDate: LocalDate
)
