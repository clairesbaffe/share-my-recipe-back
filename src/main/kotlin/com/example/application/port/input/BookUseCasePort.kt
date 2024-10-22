package com.example.application.port.input

import com.example.domain.model.Book

interface BookUseCasePort {
    suspend fun findBookById(bookId: Long): Book?
    suspend fun postBook(title: String, author: String): Book
    suspend fun findAllBooks(): List<Book>
}