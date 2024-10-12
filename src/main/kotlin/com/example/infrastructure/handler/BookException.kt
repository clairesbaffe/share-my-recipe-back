package com.example.infrastructure.handler

import com.example.infrastructure.exception.BookNotFound
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.plugins.statuspages.*

fun StatusPagesConfig.bookExceptionHandler() {
    exception<BookNotFound> { call, cause ->
        call.respondText(text = cause.localizedMessage, status = HttpStatusCode.NotFound)
    }
}