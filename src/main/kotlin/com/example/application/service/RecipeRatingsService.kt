package com.example.application.service

import com.example.application.port.input.RecipeRatingsUseCasePort
import com.example.application.port.output.RecipeRatingsLoaderPort
import com.example.domain.model.RecipeRating
import org.koin.core.annotation.Single

@Single
class RecipeRatingsService(
    private val recipeRatingsLoaderPort: RecipeRatingsLoaderPort
) : RecipeRatingsUseCasePort {

    override suspend fun getRatingsForRecipe(id: Long, page: Int, limit: Int): List<RecipeRating> {
        return recipeRatingsLoaderPort.getRatingsForRecipe(id, page, limit)
    }

    override suspend fun getOverallRatingForRecipe(id: Long): Float {
        return recipeRatingsLoaderPort.getOverallRatingForRecipe(id)
    }

    override suspend fun getRatingsForRecipeByUser(userId: Long, recipeId: Long): List<RecipeRating> {
        return recipeRatingsLoaderPort.getRatingsForRecipeByUser(userId, recipeId)
    }

    override suspend fun getRatingsByUser(userId: Long, page: Int, limit: Int): List<RecipeRating> {
        return recipeRatingsLoaderPort.getRatingsByUser(userId, page, limit)
    }

    override suspend fun getAllRates(page: Int, limit: Int): List<RecipeRating> {
        return recipeRatingsLoaderPort.getAllRates(page, limit)
    }

    override suspend fun deleteRating(userId: Long, recipeId: Long): RecipeRating? {
        return recipeRatingsLoaderPort.deleteRating(userId, recipeId)
    }

    override suspend fun patchRating(userId: Long, recipeId: Long, rating: Float): RecipeRating? {
        return recipeRatingsLoaderPort.patchRating(userId, recipeId, rating)
    }
}