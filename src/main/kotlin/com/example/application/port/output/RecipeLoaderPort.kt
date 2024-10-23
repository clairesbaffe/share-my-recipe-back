package com.example.application.port.output

import com.example.domain.model.Recipe

interface RecipeLoaderPort {
    suspend fun loadRecipe(recipeId: Long, ): Recipe?
    suspend fun saveRecipe(recipe: Recipe): Recipe
    suspend fun findAllRecipe(): List<Recipe>
    suspend fun getRecipeWithRate(page: Int, limit: Int, ): List<Pair<Recipe, Float>>
    suspend fun getRecipeOrderBy(order: String, sortBy: String, page: Int, limit: Int): List<Pair<Recipe, Float>>
    suspend fun getRecipeByIdWithRate(recipe: Recipe): Pair<Recipe, Float>?
    suspend fun getRecipeByUser(userId: Long, page: Int, limit: Int): List<Pair<Recipe, Float>>
    suspend fun deleteRecipe(userId: Long, recipeId: Long): Recipe?
}