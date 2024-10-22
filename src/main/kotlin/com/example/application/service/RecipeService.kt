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
        ratings: Float,
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
            ratings = ratings,
            authorId = authorId,
            date = date
        )
        return recipeLoaderPort.saveRecipe(recipe)
    }

    override suspend fun findAllRecipe(): List<Recipe> {
        return recipeLoaderPort.findAllRecipe()
    }

    override suspend fun updateRecipeRating(id: Long, rating: Float): Recipe {
        return recipeLoaderPort.updateRecipeRating(id, rating)
    }

    override suspend fun getRatingsForRecipe(id: Long): Float {
        return recipeLoaderPort.getRatingsForRecipe(id)
    }
}