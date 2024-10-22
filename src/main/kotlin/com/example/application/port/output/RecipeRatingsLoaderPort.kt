package com.example.application.port.output

import com.example.domain.model.RecipeRating

interface RecipeRatingsLoaderPort {
    suspend fun postRating(recipeRating: RecipeRating): RecipeRating
    suspend fun getRatingsForRecipe(id: Long): List<RecipeRating>
    suspend fun getOverallRatingForRecipe(id: Long): Float
}