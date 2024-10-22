package com.example.application.service

import com.example.application.port.input.UserUseCasePort
import com.example.application.port.output.UserRepositoryPort
import com.example.domain.model.User
import org.koin.core.annotation.Single

@Single
class UserUseCase(private val userRepository: UserRepositoryPort) : UserUseCasePort {
    override suspend fun findById(userId: Long): User? {
        return userRepository.findById(userId)
    }
}