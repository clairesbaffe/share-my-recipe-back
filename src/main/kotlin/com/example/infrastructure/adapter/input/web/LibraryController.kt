package com.example.infrastructure.adapter.input.web

import com.example.infrastructure.exception.BookNotFound
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import com.example.infrastructure.adapter.input.web.dto.BookResponseDTO
import com.example.application.port.input.BookUseCasePort
import io.ktor.server.request.*
import org.koin.ktor.ext.inject

fun Route.libraryController() {
    val bookUseCase: BookUseCasePort by inject()
    data class BookRequest(val title: String, val author: String)
    data class BookResponse(val message: String)

    route("library") {
        get("/{id}") {
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
        }
    }
    get("/libraries"){
        val books = bookUseCase.findAllBooks()
        val booksDto = books.map { book ->
            BookResponseDTO(
                id = book.id,
                title = book.title,
                author = book.author
            )
        }
        call.respond(HttpStatusCode.OK, booksDto)
    }
    post("/library"){
        val bookRequest = call.receive<BookRequest>()
        try {
            bookUseCase.postBook(bookRequest.title, bookRequest.author)
            call.respond(HttpStatusCode.Created, BookResponse("Inscription réussie"))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "Erreur lors de l'inscription")
        }
    }
}