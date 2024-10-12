package com.example.infrastructure.adapter.input.web

import com.example.infrastructure.exception.BookNotFound
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import com.example.infrastructure.adapter.input.web.dto.BookResponseDTO
import com.example.application.port.input.BookUseCasePort
import org.koin.ktor.ext.inject

fun Route.libraryController() {
    val bookUseCase: BookUseCasePort by inject()

    route("/library") {
        get("/{id}") {
            val bookId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid or missing book ID")

            val book = bookUseCase.findBookById(bookId)
                ?: throw BookNotFound("Book not found")

            val bookDto = BookResponseDTO(
                id = book.id,
                title = book.title,
                author = book.author
            )
            call.respond(HttpStatusCode.OK, bookDto)
        }
    }
}