package com.example.application.port.input

import com.example.domain.model.User

interface UserUseCasePort {
    suspend fun findById(userId: Long): User?
}