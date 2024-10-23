package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.RecipeLoaderPort
import com.example.domain.model.Recipe
import com.example.infrastructure.adapter.input.web.dto.RecipeRating
import com.example.infrastructure.adapter.output.entity.RecipeEntity
import com.example.infrastructure.adapter.output.entity.RecipeRatingsEntity
import com.example.infrastructure.adapter.output.entity.RecipeRatingsTable
import com.example.infrastructure.adapter.output.entity.RecipeTable
import com.example.infrastructure.mapper.RecipeMapper
import com.example.infrastructure.mapper.RecipeRatingsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single

@Single
class RecipeRepository : RecipeLoaderPort {
    override suspend fun loadRecipe(recipeId: Long): Recipe? {
        return withContext(Dispatchers.IO) {
            transaction {
                RecipeEntity.findById(recipeId)?.let { RecipeMapper.toDomain(it) }
            }
        }
    }

    override suspend fun saveRecipe(recipe: Recipe): Recipe {
        return withContext(Dispatchers.IO) {
            transaction {
                val entity = RecipeMapper.toEntity(recipe)
                RecipeMapper.toDomain(entity)
            }
        }
    }

    override suspend fun findAllRecipe(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            transaction {
                RecipeEntity.all().map { RecipeMapper.toDomain(it) }
            }
        }
    }

    override suspend fun getRecipeByUser(userId: Long): List<Pair<Recipe, Float>>{
        return withContext(Dispatchers.IO){
            transaction {
                val recipes = RecipeEntity.find {
                    RecipeTable.authorId eq userId
                }.toList()

                recipes.map { recipeEntity ->
                    val recipe = RecipeMapper.toDomain(recipeEntity)

                    val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq recipe.id }.toList()

                    val averageRating = if (ratings.isNotEmpty()) {
                        ratings.map { it.rating }.average().toFloat()
                    } else {
                        0f
                    }

                    recipe to averageRating
                }
            }
        }
    }


    override suspend fun getRecipeWithRate(): List<Pair<Recipe, Float>> {
        return withContext(Dispatchers.IO) {
            transaction {
                val recipes = RecipeEntity.all().toList() // Fetch all recipes
                recipes.map { recipeEntity ->
                    val recipe = RecipeMapper.toDomain(recipeEntity) // Convert to domain Recipe model

                    val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq recipe.id }.toList()

                    val averageRating = if (ratings.isNotEmpty()) {
                        ratings.map { it.rating }.average().toFloat()
                    } else {
                        0f
                    }

                    recipe to averageRating // Return a pair of Recipe and average rating
                }
            }
        }
    }


    override suspend fun getRecipeOrderBy(order: String, sortBy: String): List<Pair<Recipe, Float>> {
        return withContext(Dispatchers.IO) {
            transaction {
                val recipes = RecipeEntity.all().toList()

                val recipesWithRatings = recipes.map { recipeEntity ->
                    val recipe = RecipeMapper.toDomain(recipeEntity)

                    val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq recipe.id }.toList()

                    val averageRating = if (ratings.isNotEmpty()) {
                        ratings.map { it.rating }.average().toFloat()
                    } else {
                        0f
                    }

                    recipe to averageRating
                }

                val comparator = when (sortBy) {
                    "ratings" -> compareBy<Pair<Recipe, Float>> { it.second }
                    "dates" -> compareBy { it.first.date }
                    "difficulty" -> compareBy { it.first.difficulty }
                    else -> compareBy { it.second }
                }

                if (order == "desc") {
                    recipesWithRatings.sortedWith(comparator.reversed())
                } else {
                    recipesWithRatings.sortedWith(comparator)
                }
            }
        }
    }

    override suspend fun getRecipeByIdWithRate(recipe: Recipe): Pair<Recipe, Float>? {
        return withContext(Dispatchers.IO) {
            transaction {

                val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq recipe.id }.toList()

                val averageRating = if (ratings.isNotEmpty()) {
                    ratings.map { it.rating }.average().toFloat()
                } else {
                    0f
                }

                recipe to averageRating
            }
        }
    }

    override suspend fun deleteRecipe(userId: Long, recipeId: Long): Recipe?{
        return withContext(Dispatchers.IO) {
            transaction {

                val ratingToDelete = RecipeRatingsEntity.find {
                    (RecipeRatingsTable.recipeId eq recipeId)
                }.singleOrNull()

                ratingToDelete?.let {
                    val domainRating = RecipeRatingsMapper.toDomain(it)
                    it.delete()
                    domainRating
                }
                val recipeToDelete = RecipeEntity.find {
                    (RecipeTable.id eq recipeId) and (RecipeTable.authorId eq userId)
                }.singleOrNull()

                recipeToDelete?.let {
                    val domainRating = RecipeMapper.toDomain(it)
                    it.delete()
                    domainRating
                }
            }
        }
    }


}