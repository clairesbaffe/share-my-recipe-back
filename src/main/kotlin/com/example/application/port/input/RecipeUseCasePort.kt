package com.example.application.port.input

import com.example.domain.model.Recipe
import java.time.LocalDate

interface RecipeUseCasePort {
    suspend fun findRecipeById(recipeId: Long): Recipe?
    suspend fun postRecipe(
        title: String,
        image: String,
        description: String,
        recette: String,
        preparationTime: Float,
        nbPersons: Int,
        difficulty: Float,
        tags: List<String>,
        authorId: Long,
        date: LocalDate
    ): Recipe
    suspend fun findAllRecipe(): List<Recipe>
}