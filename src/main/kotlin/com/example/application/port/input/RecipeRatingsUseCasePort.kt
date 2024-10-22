package com.example.application.port.input

import com.example.domain.model.RecipeRating

interface RecipeRatingsUseCasePort {
   suspend fun postRating(userId: Long, recipeId: Long, rating: Float): RecipeRating
   suspend fun getRatingsForRecipe(id: Long): List<RecipeRating>
   suspend fun getOverallRatingForRecipe(id: Long): Float
}