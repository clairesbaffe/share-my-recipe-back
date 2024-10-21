package com.example.infrastructure.config

import com.example.infrastructure.adapter.input.web.libraryController
import com.example.infrastructure.adapter.input.web.loginController
import com.example.infrastructure.adapter.input.web.registerController
import com.example.infrastructure.middleware.withRole
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.routing.*

fun Application.configureRouting(config: ApplicationConfig) {
    routing {
        route("/api/v1") {
            registerController()
            loginController(config)

            withRole("USER") {
                libraryController()
            }

            withRole("ADMIN") {
            }
        }
    }
}