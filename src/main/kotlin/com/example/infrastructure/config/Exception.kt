package com.example.infrastructure.config

import com.example.infrastructure.handler.genericExceptionHandler
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        genericExceptionHandler()
    }
}