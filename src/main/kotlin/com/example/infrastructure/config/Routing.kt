package com.example.infrastructure.config

import com.example.infrastructure.adapter.input.web.libraryController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            libraryController()
        }
    }
}