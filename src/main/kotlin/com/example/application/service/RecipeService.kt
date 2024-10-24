package com.example.application.service

import com.example.application.port.input.RecipeUseCasePort
import com.example.application.port.output.RecipeLoaderPort
import com.example.domain.model.Recipe
import com.example.domain.model.User
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class RecipeService(
    private val recipeLoaderPort: RecipeLoaderPort
) : RecipeUseCasePort {

    override suspend fun findRecipeById(recipeId: Long): Recipe? {
        return recipeLoaderPort.loadRecipe(recipeId)
    }

    override suspend fun postRecipe(
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
    ): Recipe {
        val recipe = Recipe(
            id = 0,
            title = title,
            image = image,
            description = description,
            recette = recette,
            preparationTime = preparationTime,
            nbPersons = nbPersons,
            difficulty = difficulty,
            tags = tags,
            authorId = authorId,
            date = date
        )
        return recipeLoaderPort.saveRecipe(recipe)
    }

    override suspend fun findAllRecipe(): List<Recipe> {
        return recipeLoaderPort.findAllRecipe()
    }

    override suspend fun getRecipeWithRate(page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return recipeLoaderPort.getRecipeWithRate(page, limit)
    }

    override suspend fun getRecipeOrderBy(
        order: String,
        sortBy: String,
        page: Int,
        limit: Int
    ): List<Pair<Recipe, Float>> {
        return recipeLoaderPort.getRecipeOrderBy(order, sortBy, page, limit)
    }

    override suspend fun getRecipeByIdWithRate(recipe: Recipe): Triple<Recipe, Float, User>? {
        return recipeLoaderPort.getRecipeByIdWithRate(recipe)
    }

    override suspend fun getRecipeByUser(userId: Long, page: Int, limit: Int): List<Triple<Recipe, Float, User>> {
        return recipeLoaderPort.getRecipeByUser(userId, page, limit)
    }

    override suspend fun deleteRecipe(userId: Long, recipeId: Long): Recipe? {
        return recipeLoaderPort.deleteRecipe(userId, recipeId)
    }

    override suspend fun searchRecipeWithStr(str: String, page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return recipeLoaderPort.searchRecipeWithStr(str, page, limit)
    }

    override suspend fun getByIngredients(ingredients: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return recipeLoaderPort.getByIngredients(ingredients, page, limit)
    }

    override suspend fun getByFilters(
        order: String, sortBy: String,
        nbPersons: List<Int>, preparationTime: List<Int>,
        exclusions: List<String>,
        difficulty: Int,
        tags: List<String>,
        page: Int,
        limit: Int
    ): List<Pair<Recipe, Float>> {
        return recipeLoaderPort.getByFilters(
            order,
            sortBy,
            nbPersons,
            preparationTime,
            exclusions,
            difficulty,
            tags,
            page,
            limit
        )
    }

    override suspend fun getByTagsAny(tags: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return recipeLoaderPort.getByTagsAny(tags, page, limit)
    }

    override suspend fun getByTagsAll(tags: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return recipeLoaderPort.getByTagsAll(tags, page, limit)
    }

    override suspend fun getRecipeByIdByUserSession(recipeId: Long, userId: Long): Pair<Recipe, Float>? {
        return recipeLoaderPort.getRecipeByIdByUserSession(recipeId, userId)
    }


    override suspend fun getByFiltersWithQuery(
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
    ): List<Pair<Recipe, Float>> {
        return recipeLoaderPort.getByFiltersWithQuery(
            query, order, sortBy, nbPersons, preparationTime, exclusions, difficulty, tags, page, limit
        )
    }
}