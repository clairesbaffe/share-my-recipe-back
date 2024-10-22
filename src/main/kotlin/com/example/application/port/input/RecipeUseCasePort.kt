package com.example.application.port.input

import com.example.domain.model.Recipe

interface RecipeUseCasePort {
    suspend fun loadRecipe(recipeId: Long): Recipe?
    suspend fun saveRecipe(recipe: Recipe): Recipe
    suspend fun findAllRecipe(): List<Recipe>
}