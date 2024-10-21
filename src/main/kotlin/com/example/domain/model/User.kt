package com.example.domain.model

data class User(
    val id: Long,
    val username: String,
    val passwordHash: String,
    val roles: List<String>
)