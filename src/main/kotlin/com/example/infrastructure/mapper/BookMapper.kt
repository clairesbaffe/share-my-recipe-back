package com.example.infrastructure.mapper

import com.example.domain.model.Book
import com.example.infrastructure.adapter.output.entity.BookEntity

object BookMapper {
    fun toDomain(entity: BookEntity): Book {
        return Book(entity.id.value, entity.title, entity.author)
    }

    fun toEntity(book: Book): BookEntity {
        return BookEntity.new {
            this.title = book.title
            this.author = book.author
        }
    }
}