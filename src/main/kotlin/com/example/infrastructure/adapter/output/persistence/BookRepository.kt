package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.BookLoaderPort
import com.example.domain.model.Book
import com.example.infrastructure.adapter.output.entity.BookEntity
import com.example.infrastructure.mapper.BookMapper
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class BookRepository : BookLoaderPort {
    override suspend fun loadBook(bookId: Long): Book? {
        return withContext(Dispatchers.IO) {
            transaction {
                BookEntity.findById(bookId)?.let { BookMapper.toDomain(it) }
            }
        }
    }
}