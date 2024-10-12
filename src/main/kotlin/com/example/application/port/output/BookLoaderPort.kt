package com.example.application.port.output

import com.example.domain.model.Book

interface BookLoaderPort {
    suspend fun loadBook(bookId: Long): Book?
}