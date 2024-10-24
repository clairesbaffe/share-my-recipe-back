package com.example.application.port.input

import com.example.domain.model.Recipe
import com.example.domain.model.User
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
    suspend fun getRecipeWithRate(page: Int, limit: Int): List<Pair<Recipe, Float>>
    suspend fun getRecipeOrderBy(order: String, sortBy: String, page: Int, limit: Int): List<Pair<Recipe, Float>>
    suspend fun getRecipeByIdWithRate(recipe: Recipe): Triple<Recipe, Float, User>?
    suspend fun getRecipeByUser(userId: Long, page: Int, limit: Int): List<Triple<Recipe, Float, User>>
    suspend fun deleteRecipe(userId: Long, recipeId: Long): Recipe?
    suspend fun searchRecipeWithStr(str: String, page: Int, limit: Int): List<Pair<Recipe, Float>>
    suspend fun getByIngredients(ingredients: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>>
    suspend fun getByFilters(order: String, sortBy: String, nbPersons: List<Int>, preparationTime: List<Int>, exclusions: List<String>, difficulty: Int, tags: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>>
    suspend fun getByFiltersWithQuery(
        query: String,
        order: String,
        sortBy: String,
        nbPersons: List<Int>,
        preparationTime: List<Int>,
        exclusions: List<String>,
        difficulty: Int,
        tags: List<String>,
        page: Int,
        limit: Int
    ): List<Pair<Recipe, Float>>
    suspend fun getByTagsAny(tags: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>>
    suspend fun getByTagsAll(tags: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>>
    suspend fun getRecipeByIdByUserSession(recipeId: Long, userId: Long): Pair<Recipe, Float>?

}