package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.RecipeRatingsLoaderPort
import com.example.domain.model.RecipeRating
import com.example.infrastructure.adapter.output.entity.RecipeRatingsEntity
import com.example.infrastructure.adapter.output.entity.RecipeRatingsTable
import com.example.infrastructure.mapper.RecipeRatingsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single

@Single
class RecipeRatingsRepository : RecipeRatingsLoaderPort {
    override suspend fun postRating(recipeRating: RecipeRating): RecipeRating {
        return withContext(Dispatchers.IO) {
            transaction {
                val entity = RecipeRatingsMapper.toEntity(recipeRating)
                RecipeRatingsMapper.toDomain(entity)
            }
        }
    }

    override suspend fun getRatingsForRecipe(recipeId: Long): List<RecipeRating> {
        return withContext(Dispatchers.IO) {
            transaction {
                val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq recipeId }.toList()

                if (ratings.isEmpty()) {
                    throw IllegalArgumentException("Aucune évaluation trouvée pour cette recette avec l'ID: $recipeId")
                }

                ratings.map { RecipeRatingsMapper.toDomain(it) }
            }
        }
    }

    override suspend fun getOverallRatingForRecipe(id: Long): Float {
        return withContext(Dispatchers.IO){
            transaction{
                val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq id }.toList()

                if (ratings.isEmpty()) {
                    throw IllegalArgumentException("Aucune évaluation trouvée pour cette recette avec l'ID: $id")
                }

                var allRatings = ratings.map { RecipeRatingsMapper.toDomain(it) }
                var overall = 0f
                for (rating in allRatings) {
                    overall = overall + rating.rating
                }
                overall / allRatings.size
            }
        }
    }

}