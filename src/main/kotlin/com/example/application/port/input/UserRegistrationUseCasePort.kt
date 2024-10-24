package com.example.application.port.input

import com.example.domain.model.User
import java.time.LocalDate

interface UserRegistrationUseCasePort {
    suspend fun registerUser(username: String, password: String, date: LocalDate): User
}