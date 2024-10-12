package com.example.application.port.input

import com.example.domain.model.Book

interface BookUseCasePort {
    suspend fun findBookById(bookId: Long): Book?
}