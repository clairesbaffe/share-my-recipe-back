package com.example.application.port.output

import com.example.domain.model.Recipe

interface RecipeLoaderPort {
    suspend fun loadRecipe(recipeId: Long): Recipe?
    suspend fun saveRecipe(recipe: Recipe): Recipe
    suspend fun findAllRecipe(): List<Recipe>
}