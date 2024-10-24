package com.example.infrastructure.adapter.input.web

import com.example.application.port.input.UserRegistrationUseCasePort
import com.example.application.port.input.UserUseCasePort
import com.example.infrastructure.adapter.input.web.dto.UserResponseDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.koin.ktor.ext.inject
import java.time.LocalDate

fun Route.publicUserController() {
    val userUseCase: UserUseCasePort by inject()

    get("/{userId}") {
        try {
            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID recette invalide ou manquant")
            val user = userUseCase.findById(userId)
                ?: throw IllegalArgumentException("utilisateur introuvable")

            val userResponseDTO = UserResponseDTO(
                id = user.id,
                username = user.username,
                password = user.passwordHash,
                creationDate = user.date
            )

            call.respond(HttpStatusCode.OK, userResponseDTO)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "Erreur lors de l'inscription")
        }
    }
}