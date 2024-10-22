package com.example.infrastructure.middleware

import com.example.application.port.input.UserUseCasePort
import com.example.infrastructure.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.withRole(role: String, build: Route.() -> Unit) {
    val userUseCase: UserUseCasePort by inject()

    intercept(ApplicationCallPipeline.Call) {
        val userSession = call.sessions.get<UserSession>()
        if (userSession == null) {
            call.respond(HttpStatusCode.Unauthorized, "Vous devez être connecté pour accéder à cette ressource")
            finish()
        } else {
            val user = userUseCase.findById(userSession.userId)
            val userRoles = user?.roles ?: emptyList()

            if (role !in userRoles) {
                call.respond(HttpStatusCode.Forbidden, "Permissions insuffisantes")
                finish()
            }
        }
    }
    build()
}