package com.example.application.port.output

import com.example.domain.model.User

interface UserRepositoryPort {
    suspend fun findByUsername(username: String): User?
    suspend fun save(user: User): User
}