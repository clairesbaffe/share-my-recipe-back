package com.example.infrastructure.adapter.input.web.dto

data class RecipeRequest(
    val title: String,
    val image: String,
    val description: String,
    val recette: RecipeDetails,
    val preparationTime: Float,
    val nbPersons: Int,
    val difficulty: Float,
    val tags: List<String>,
    val ratings: Float,
    val authorId: Long,
    val date: String
)

data class RecipeDetails(
    val ingredients: List<String>,
    val instructions: List<String>
)