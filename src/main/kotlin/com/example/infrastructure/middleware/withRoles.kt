package com.example.infrastructure.middleware

import com.example.infrastructure.utils.hasRole
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.withRole(role: String, build: Route.() -> Unit): Route {
    return authenticate("auth-jwt") {
        route("") {
            intercept(ApplicationCallPipeline.Call) {
                if (!call.hasRole(role)) {
                    call.respond(HttpStatusCode.Forbidden, "Permissions insuffisantes")
                    finish()
                }
            }
            build()
        }
    }
}