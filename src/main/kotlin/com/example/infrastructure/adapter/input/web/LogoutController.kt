package com.example.infrastructure.adapter.input.web

import com.example.infrastructure.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.logoutController() {
    post("/logout") {
        call.sessions.clear<UserSession>()
        call.respond(HttpStatusCode.OK, "Déconnexion réussie")
    }
}