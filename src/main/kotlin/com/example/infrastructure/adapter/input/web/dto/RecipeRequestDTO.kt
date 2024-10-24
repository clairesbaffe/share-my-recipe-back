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
)

data class RecipeDetails(
    val ingredients: List<String>,
    val instructions: List<String>
)

data class RecipeRating(
    val rating: Float,
)

data class SearchRecipes(
    val search: String?
)

data class SearchRecipesByList(
    val search: List<String>?
)

data class SearchRecipesByFilters(
    val exclusions: List<String>?,
    val difficulty: Int?,
    val tags: List<String>?,
    val nbPersons: List<Int>?,
    val preparationTime: List<Int>?
)