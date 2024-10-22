package com.example.infrastructure.middleware

import com.example.infrastructure.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.withSessionRenewal(build: Route.() -> Unit) {
    intercept(ApplicationCallPipeline.Call) {
        val session = call.sessions.get<UserSession>()
        if (session != null) {
            val timeLeft = session.expiryTime - System.currentTimeMillis()
            if (timeLeft < 10 * 60 * 1000) {
                call.sessions.set(session.copy(expiryTime = System.currentTimeMillis() + 3600 * 1000))
            }
        }
    }
    build()
}