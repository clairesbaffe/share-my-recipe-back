package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.RecipeRatingsLoaderPort
import com.example.domain.model.Recipe
import com.example.domain.model.RecipeRating
import com.example.infrastructure.adapter.output.entity.RecipeEntity
import com.example.infrastructure.adapter.output.entity.RecipeRatingsEntity
import com.example.infrastructure.adapter.output.entity.RecipeRatingsTable
import com.example.infrastructure.mapper.RecipeMapper
import com.example.infrastructure.mapper.RecipeRatingsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single

@Single
class RecipeRatingsRepository : RecipeRatingsLoaderPort {

    override suspend fun getRatingsForRecipe(recipeId: Long, page: Int, limit: Int): List<RecipeRating> {
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page-1) * limit
                val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq recipeId }.limit(limit, offset = offset.toLong()).toList()

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
                var overall = 0f
                if (ratings.isEmpty()) {
                    overall
                }

                var allRatings = ratings.map { RecipeRatingsMapper.toDomain(it) }
                for (rating in allRatings) {
                    overall = overall + rating.rating
                }
                overall / allRatings.size
            }
        }
    }

    override suspend fun getRatingsForRecipeByUser(userId: Long, recipeId: Long): List<RecipeRating> {
        return withContext(Dispatchers.IO){
            transaction {
                val ratings = RecipeRatingsEntity.find {
                    (RecipeRatingsTable.recipeId eq recipeId) and (RecipeRatingsTable.userId eq userId)
                }.toList()

                if (ratings.isEmpty()) {
                    throw IllegalArgumentException("Aucune évaluation trouvée pour cette recette avec l'ID: $recipeId")
                }

                ratings.map { RecipeRatingsMapper.toDomain(it) }
            }
        }
    }

    override suspend fun getRatingsByUser(userId: Long, page: Int, limit: Int): List<RecipeRating> {
        return withContext(Dispatchers.IO){
            transaction {
                val offset = (page-1) * limit
                val ratings = RecipeRatingsEntity.find {
                    RecipeRatingsTable.userId eq userId
                }.limit(limit, offset = offset.toLong()).toList()

                if (ratings.isEmpty()) {
                    throw IllegalArgumentException("Aucune évaluation trouvée pour le user $userId")
                }

                ratings.map { RecipeRatingsMapper.toDomain(it) }
            }
        }
    }

    override suspend fun getAllRates(page: Int, limit: Int): List<RecipeRating> {
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page-1) * limit
                RecipeRatingsEntity.all().limit(limit, offset = offset.toLong()).map { RecipeRatingsMapper.toDomain(it) }
            }
        }
    }

    override suspend fun deleteRating(userId: Long, recipeId: Long): RecipeRating? {
        return withContext(Dispatchers.IO) {
            transaction {
                val ratingToDelete = RecipeRatingsEntity.find {
                    (RecipeRatingsTable.recipeId eq recipeId) and (RecipeRatingsTable.userId eq userId)
                }.singleOrNull()

                ratingToDelete?.let {
                    val domainRating = RecipeRatingsMapper.toDomain(it)
                    it.delete()
                    domainRating
                }
            }
        }
    }

    override suspend fun patchRating(userId: Long, recipeId: Long, rating: Float): RecipeRating? {
        return withContext(Dispatchers.IO) {
            transaction {
                val existingRecipe = RecipeRatingsEntity.find {
                    (RecipeRatingsTable.recipeId eq recipeId) and (RecipeRatingsTable.userId eq userId)
                }.singleOrNull()

                val updatedRecipe = if (existingRecipe != null) {
                    existingRecipe?.apply {
                        this.rating = rating
                    } ?: throw IllegalArgumentException("No rating found for user $userId for recipe $recipeId")

                } else{
                    RecipeRatingsEntity.new {
                        this.userId = userId
                        this.recipeId = recipeId
                        this.rating = rating
                    }
                }
                RecipeRatingsMapper.toDomain(updatedRecipe)
            }
        }
    }
}