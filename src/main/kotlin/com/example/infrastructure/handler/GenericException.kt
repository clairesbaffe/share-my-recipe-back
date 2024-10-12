package com.example.infrastructure.handler

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.plugins.statuspages.*

fun StatusPagesConfig.genericExceptionHandler() {
    exception<Throwable> { call, _ ->
        call.respondText(text = "Internal server error", status = HttpStatusCode.InternalServerError)
    }
}