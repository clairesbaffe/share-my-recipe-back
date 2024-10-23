package com.example.application.service

import com.example.application.port.input.RecipeUseCasePort
import com.example.application.port.output.RecipeLoaderPort
import com.example.domain.model.Recipe
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

    override suspend fun getRecipeWithRate(page: Int, limit: Int): List<Pair<Recipe, Float>>{
        return recipeLoaderPort.getRecipeWithRate(page, limit)
    }

    override suspend fun getRecipeOrderBy(order: String, sortBy: String, page: Int, limit: Int): List<Pair<Recipe, Float>>{
        return recipeLoaderPort.getRecipeOrderBy(order, sortBy, page, limit)
    }

    override suspend fun getRecipeByIdWithRate(recipe: Recipe): Pair<Recipe, Float>?{
        return recipeLoaderPort.getRecipeByIdWithRate(recipe)
    }

    override suspend fun getRecipeByUser(userId: Long, page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return recipeLoaderPort.getRecipeByUser(userId, page, limit)
    }

    override suspend fun deleteRecipe(userId: Long, recipeId: Long): Recipe?{
        return recipeLoaderPort.deleteRecipe(userId, recipeId)
    }



}