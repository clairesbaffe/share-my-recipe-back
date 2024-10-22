package com.example.application.service

import com.example.application.port.input.RecipeRatingsUseCasePort
import com.example.application.port.output.RecipeRatingsLoaderPort
import com.example.domain.model.RecipeRating
import org.koin.core.annotation.Single

@Single
class RecipeRatingsService(
    private val recipeRatingsLoaderPort: RecipeRatingsLoaderPort
) : RecipeRatingsUseCasePort {
    override suspend fun postRating(userId: Long, recipeId: Long, rating: Float): RecipeRating {

        val recipeRating = RecipeRating(
            id = 0,
            userid = userId,
            recipeid = recipeId,
            rating = rating
        )
        return recipeRatingsLoaderPort.postRating(recipeRating)
    }

    override suspend fun getRatingsForRecipe(id: Long): List<RecipeRating> {
        return recipeRatingsLoaderPort.getRatingsForRecipe(id)
    }

    override suspend fun getOverallRatingForRecipe(id: Long): Float {
        return recipeRatingsLoaderPort.getOverallRatingForRecipe(id)
    }
}