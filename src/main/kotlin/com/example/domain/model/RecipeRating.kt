package com.example.domain.model

data class RecipeRating(
    val userId: Long,
    val recipeId: Long,
    val rating: Float,
)