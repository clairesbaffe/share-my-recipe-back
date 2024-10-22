package com.example.application.service

import com.example.application.port.input.UserRegistrationUseCasePort
import com.example.application.port.output.UserRepositoryPort
import com.example.domain.model.User
import org.koin.core.annotation.Single
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDate

@Single
class UserRegistrationService(
    private val userRepository: UserRepositoryPort
) : UserRegistrationUseCasePort {

    override suspend fun registerUser(username: String, password: String, date: LocalDate): User {
        val existingUser = userRepository.findByUsername(username)
        if (existingUser != null) {
            throw IllegalArgumentException("Nom d'utilisateur déjà pris")
        }

        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
        val user = User(
            id = 0,
            username = username,
            passwordHash = passwordHash,
            roles = listOf("USER"),
            date = date
        )
        return userRepository.save(user)
    }
}