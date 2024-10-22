package com.example.infrastructure.adapter.input.web

import com.example.application.port.input.UserRegistrationUseCasePort
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.koin.ktor.ext.inject

data class RegisterRequest(val username: String, val password: String)

fun Route.registerController() {
    val userRegistrationUseCase: UserRegistrationUseCasePort by inject()

    post("/register") {
        val registerRequest = call.receive<RegisterRequest>()
        try {
            userRegistrationUseCase.registerUser(registerRequest.username, registerRequest.password)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Inscription réussie"))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "Erreur lors de l'inscription")
        }
    }
}