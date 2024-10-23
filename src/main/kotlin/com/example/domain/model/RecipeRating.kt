package com.example.domain.model

data class RecipeRating(
    val id: Long,
    val userid: Long,
    val recipeid: Long,
    val rating: Float,
)