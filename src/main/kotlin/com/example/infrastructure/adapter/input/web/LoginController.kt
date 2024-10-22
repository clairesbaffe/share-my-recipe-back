package com.example.infrastructure.adapter.input.web

import com.example.application.port.input.UserAuthenticationUseCasePort
import com.example.infrastructure.adapter.input.web.dto.LoginRequest
import com.example.infrastructure.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.koin.ktor.ext.inject
import io.ktor.server.sessions.*
import java.util.concurrent.TimeUnit

fun Route.loginController() {
    val userAuthUseCase: UserAuthenticationUseCasePort by inject()

    post("/login") {
        val loginRequest = call.receive<LoginRequest>()
        val user = userAuthUseCase.authenticate(loginRequest.username, loginRequest.password)

        if (user != null) {
            val expiryTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)

            val userSession = UserSession(userId = user.id, username = user.username, expiryTime = expiryTime)
            call.sessions.set(userSession)

            call.respond(HttpStatusCode.OK, mapOf("message" to "Connexion réussie"))
        } else {
            call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Identifiants invalides"))
        }
    }
}