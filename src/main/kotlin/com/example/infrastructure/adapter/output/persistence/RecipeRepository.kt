package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.RecipeLoaderPort
import com.example.domain.model.Recipe
import com.example.infrastructure.adapter.input.web.dto.RecipeRating
import com.example.infrastructure.adapter.output.entity.RecipeEntity
import com.example.infrastructure.adapter.output.entity.RecipeRatingsEntity
import com.example.infrastructure.adapter.output.entity.RecipeRatingsTable
import com.example.infrastructure.mapper.RecipeMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
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

    override suspend fun updateRecipeRating(id: Long, rating: Float): Recipe {
        return withContext(Dispatchers.IO) {
            transaction {
                val existingEntity = RecipeEntity.findById(id)
                    ?: throw IllegalArgumentException("Recette non trouvée")

//                var rate = (existingEntity.ratings + rating) / 2
//                rate = if (rate > 5) 5.0f else rate
                existingEntity.ratings = rating

                RecipeMapper.toDomain(existingEntity)
            }
        }
    }

    override suspend fun getRatingsForRecipe(id: Long): Float {
//        return withContext(Dispatchers.IO) {
//            transaction {
//                val existingRatings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq id }.toList()
//
//                if (existingRatings.isEmpty()) {
//                    throw IllegalArgumentException("Aucune évaluation trouvée pour cette recette")
//                }
//
//                var overall = 0f
//                for (rating in existingRatings) {
//                    overall += rating.ratings
//                }
//
//                val average = overall / existingRatings.size
//
//                return@transaction average
//            }
//        }
        TODO("Not yet implemented")

    }


}