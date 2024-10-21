package com.example.application.port.input

import com.example.domain.model.User

interface UserAuthenticationUseCasePort {
    suspend fun authenticate(username: String, password: String): User?
}