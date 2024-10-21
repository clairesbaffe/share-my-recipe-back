package com.example.infrastructure.adapter.input.web

import com.example.infrastructure.exception.BookNotFound
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import com.example.infrastructure.adapter.input.web.dto.BookResponseDTO
import com.example.application.port.input.BookUseCasePort
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Route.libraryController() {
    val bookUseCase: BookUseCasePort by inject()

    route("/library") {
        get("/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val roles = principal?.getClaim("roles", List::class)?.filterIsInstance<String>() ?: emptyList()

            if ("ROLE_USER" in roles) {
                val bookId = call.parameters["id"]?.toLongOrNull()
                    ?: throw IllegalArgumentException("ID de livre invalide ou manquant")

                val book = bookUseCase.findBookById(bookId)
                    ?: throw BookNotFound("Livre non trouvé")

                val bookDto = BookResponseDTO(
                    id = book.id,
                    title = book.title,
                    author = book.author
                )
                call.respond(HttpStatusCode.OK, bookDto)
            } else {
                call.respond(HttpStatusCode.Forbidden, "Permissions insuffisantes")
            }
        }
    }
}