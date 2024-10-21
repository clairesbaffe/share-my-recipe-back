package com.example.infrastructure.config

import com.example.infrastructure.adapter.input.web.libraryController
import com.example.infrastructure.adapter.input.web.loginController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.routing.*
import registerController

fun Application.configureRouting(config: ApplicationConfig) {
    routing {
        route("/api/v1") {
            registerController()
            loginController(config)
        }
        authenticate("auth-jwt") {
            libraryController()
        }
    }
}