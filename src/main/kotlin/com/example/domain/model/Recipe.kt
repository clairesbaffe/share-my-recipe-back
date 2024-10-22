package com.example.domain.model

import java.time.LocalDate

data class Recipe(
    val id: Long,
    val title: String,
    val image: String,
    val description: String,
    val recette: String,
    val preparationTime: Float,
    val nbPersons: Int,
    val difficulty: Float,
    val tags: List<String>,
    var ratings: Float,
    val authorId: Long,
    val date: LocalDate
)