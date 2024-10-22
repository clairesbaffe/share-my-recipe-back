package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.BookLoaderPort
import com.example.application.port.output.RecipeLoaderPort
import com.example.domain.model.Book
import com.example.domain.model.Recipe
import com.example.infrastructure.adapter.output.entity.BookEntity
import com.example.infrastructure.adapter.output.entity.RecipeEntity
import com.example.infrastructure.mapper.BookMapper
import com.example.infrastructure.mapper.RecipeMapper
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
                RecipeEntity.findById(recipeId)?.let { RecipeMapper.toDomain(it) }
            }
        }
    }

    override suspend fun saveRecipe(recipe: Recipe): Recipe {
        return withContext(Dispatchers.IO) {
            transaction {
                val entity = RecipeMapper.toEntity(recipe)
                RecipeMapper.toDomain(entity)
            }
        }
    }

    override suspend fun findAllRecipe(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            transaction {
                RecipeEntity.all().map { RecipeMapper.toDomain(it) }
            }
        }
    }
}