package com.example.application.port.output

import com.example.domain.model.RecipeRating

interface RecipeRatingsLoaderPort {
    suspend fun postRating(recipeRating: RecipeRating): RecipeRating
}