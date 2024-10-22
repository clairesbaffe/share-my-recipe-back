package com.example.infrastructure.config

import com.example.infrastructure.adapter.input.web.libraryController
import com.example.infrastructure.adapter.input.web.loginController
import com.example.infrastructure.adapter.input.web.logoutController
import com.example.infrastructure.adapter.input.web.registerController
import com.example.infrastructure.middleware.withRole
import com.example.infrastructure.middleware.withSessionRenewal
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            registerController()
            loginController()
            logoutController()

            withSessionRenewal {
                route("/library") {
                    withRole("USER") {
                        libraryController()
                    }
                }

                route("/admin") {
                    withRole("ADMIN") {
                    }
                }
            }
        }
    }
}