package com.example.infrastructure.adapter.input.web

import com.example.application.port.input.UserUseCasePort
import com.example.infrastructure.adapter.input.web.dto.UserMeResponseDTO
import com.example.infrastructure.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.meController() {
    val userUseCase: UserUseCasePort by inject()
    get("/me") {
        try {
            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")
            val user = userUseCase.findById(userSession.userId)
                ?: throw IllegalArgumentException("utilisateur introuvable")

            val userResponseDTO = UserMeResponseDTO(
                id = user.id,
                username = user.username,
                creationDate = user.date,
                authenticated = true
            )

            call.respond(HttpStatusCode.OK, userResponseDTO)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "Erreur lors de l'inscription")
        }
    }
}