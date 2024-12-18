package com.example.infrastructure.adapter.input.web.dto

import java.time.LocalDate

data class RecipeResponseDTO(
    val id: Long,
    val title: String,
    val image: String,
    val description: String,
    val recette: RecipeDetails,
    val preparationTime: Float,
    val nbPersons: Int,
    val difficulty: Float,
    val tags: List<String>,
    val authorId: Long,
    val date: LocalDate
)

data class RecipeWithRatingResponseDTO(
    val id: Long,
    val title: String,
    val image: String,
    val description: String,
    val recette: RecipeDetails,
    val preparationTime: Float,
    val nbPersons: Int,
    val difficulty: Float,
    val tags: List<String>,
    val authorId: Long,
    val date: LocalDate,
    val rating: Float
)

data class RecipeWithRatingAndUsernameResponseDTO(
    val id: Long,
    val title: String,
    val image: String,
    val description: String,
    val recette: RecipeDetails,
    val preparationTime: Float,
    val nbPersons: Int,
    val difficulty: Float,
    val tags: List<String>,
    val authorId: Long,
    val authorName: String,
    val date: LocalDate,
    val rating: Float
)