package com.example.application.port.output

import com.example.domain.model.Book

interface BookLoaderPort {
    suspend fun loadBook(bookId: Long): Book?
    suspend fun saveBook(book: Book): Book
    suspend fun findAllBooks(): List<Book>
}