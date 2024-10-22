package com.example.infrastructure.adapter.input.web

import com.example.infrastructure.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.meController() {
    get("/me") {
        val userSession = call.sessions.get<UserSession>()

        if (userSession != null) {
            call.respond(HttpStatusCode.OK, mapOf(
                "authenticated" to true,
                "user" to mapOf("id" to userSession.userId, "username" to userSession.username)
            ))
        } else {
            call.respond(HttpStatusCode.OK, mapOf("authenticated" to false))
        }
    }
}