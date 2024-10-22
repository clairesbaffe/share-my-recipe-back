package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.RecipeRatingsLoaderPort
import com.example.domain.model.RecipeRating
import com.example.infrastructure.mapper.RecipeMapper
import com.example.infrastructure.mapper.RecipeRatingsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction

class RecipeRatingsRepository : RecipeRatingsLoaderPort {
    override suspend fun postRating(recipeRating: RecipeRating): RecipeRating {
        return withContext(Dispatchers.IO) {
            transaction {
                val entity = RecipeRatingsMapper.toEntity(recipeRating)
                RecipeRatingsMapper.toDomain(entity)
            }
        }
    }
}