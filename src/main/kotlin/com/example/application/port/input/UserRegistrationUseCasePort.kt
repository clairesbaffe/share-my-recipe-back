package com.example.application.port.input

import com.example.domain.model.User

interface UserRegistrationUseCasePort {
    suspend fun registerUser(username: String, password: String): User
}