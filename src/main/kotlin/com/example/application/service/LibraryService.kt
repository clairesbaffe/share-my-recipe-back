package com.example.application.service

import com.example.application.port.input.BookUseCasePort
import com.example.application.port.output.BookLoaderPort
import com.example.domain.model.Book
import org.koin.core.annotation.Single

@Single
class LibraryService(
    private val bookLoaderPort: BookLoaderPort
) : BookUseCasePort {
    override suspend fun findBookById(bookId: Long): Book? {
        return bookLoaderPort.loadBook(bookId)
    }
}