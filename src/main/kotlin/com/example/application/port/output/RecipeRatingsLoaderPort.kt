package com.example.application.port.output

import com.example.domain.model.RecipeRating

interface RecipeRatingsLoaderPort {
    suspend fun getRatingsForRecipe(id: Long, page: Int, limit: Int): List<RecipeRating>
    suspend fun getOverallRatingForRecipe(id: Long): Float
    suspend fun getRatingsForRecipeByUser(userId: Long, recipeId: Long): List<RecipeRating>
    suspend fun getRatingsByUser(userId: Long,page: Int, limit: Int): List<RecipeRating>
    suspend fun getAllRates(page: Int, limit: Int): List<RecipeRating>
    suspend fun deleteRating(userId: Long, recipeId: Long): RecipeRating?
    suspend fun patchRating(userId: Long, recipeId: Long, rating: Float): RecipeRating?
}