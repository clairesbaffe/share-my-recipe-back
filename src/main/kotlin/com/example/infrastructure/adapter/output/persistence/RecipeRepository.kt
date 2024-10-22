package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.BookLoaderPort
import com.example.application.port.output.RecipeLoaderPort
import com.example.domain.model.Book
import com.example.domain.model.Recipe
import com.example.infrastructure.adapter.output.entity.BookEntity
import com.example.infrastructure.adapter.output.entity.RecipeEntity
import com.example.infrastructure.mapper.BookMapper
import com.example.infrastructure.mapper.UserMapper
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

@Single
class RecipeRepository : RecipeLoaderPort {
    override suspend fun loadRecipe(recipeId: Long): Recipe? {
        return withContext(Dispatchers.IO) {
            transaction {
                RecipeEntity.findById(recipeId)?.let { BookMapper.toDomain(it) }
            }
        }
    }

    override suspend fun saveBook(book: Book): Book {
        return withContext(Dispatchers.IO) {
            transaction {
                val entity = BookMapper.toEntity(book)
                BookMapper.toDomain(entity)
            }
        }
    }

    override suspend fun findAllBooks(): List<Book> {
        return withContext(Dispatchers.IO) {
            transaction {
                BookEntity.all().map { BookMapper.toDomain(it) }
            }
        }
    }
}