package com.example.application.service

import com.example.application.port.input.UserAuthenticationUseCasePort
import com.example.application.port.output.UserRepositoryPort
import com.example.domain.model.User
import org.koin.core.annotation.Single
import org.mindrot.jbcrypt.BCrypt

@Single
class UserAuthenticationService(
    private val userRepository: UserRepositoryPort
) : UserAuthenticationUseCasePort {

    override suspend fun authenticate(username: String, password: String): User? {
        val user = userRepository.findByUsername(username)
        return if (user != null && BCrypt.checkpw(password, user.passwordHash)) {
            user
        } else {
            null
        }
    }
}