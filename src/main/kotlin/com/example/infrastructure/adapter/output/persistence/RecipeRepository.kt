package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.RecipeLoaderPort
import com.example.domain.model.Recipe
import com.example.domain.model.User
import com.example.infrastructure.adapter.input.web.dto.RecipeDetails
import com.example.infrastructure.adapter.input.web.dto.RecipeRating
import com.example.infrastructure.adapter.output.entity.*
import com.example.infrastructure.mapper.RecipeMapper
import com.example.infrastructure.mapper.RecipeRatingsMapper
import com.example.infrastructure.mapper.UserMapper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
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

    override suspend fun getRecipeByUser(userId: Long, page: Int, limit: Int): List<Triple<Recipe, Float, User>> {
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page - 1) * limit
                val recipes = RecipeEntity.find {
                    RecipeTable.authorId eq userId
                }.limit(limit, offset = offset.toLong()).toList()

                recipes.map { recipeEntity ->
                    val recipe = RecipeMapper.toDomain(recipeEntity)

                    val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq recipe.id }.toList()

                    val averageRating = if (ratings.isNotEmpty()) {
                        ratings.map { it.rating }.average().toFloat()
                    } else {
                        0f
                    }

                    val userEntity = UserEntity.findById(recipeEntity.authorId)!!
                    val user = userEntity.let { UserMapper.toDomain(it) }
                    Triple(recipe, averageRating, user)
                }
            }
        }
    }


    override suspend fun getRecipeWithRate(page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page - 1) * limit
                val recipes = RecipeEntity.all().limit(limit, offset = offset.toLong()).toList()
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


    override suspend fun getRecipeOrderBy(
        order: String,
        sortBy: String,
        page: Int,
        limit: Int
    ): List<Pair<Recipe, Float>> {
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page - 1) * limit
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

                val sorted = if (order == "desc") {
                    recipesWithRatings.sortedWith(comparator.reversed())
                } else {
                    recipesWithRatings.sortedWith(comparator)
                }

                sorted.drop(offset).take(limit)
            }
        }
    }

    override suspend fun getRecipeByIdWithRate(recipe: Recipe): Triple<Recipe, Float, User>? {
        return withContext(Dispatchers.IO) {
            transaction {

                val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq recipe.id }.toList()

                val averageRating = if (ratings.isNotEmpty()) {
                    ratings.map { it.rating }.average().toFloat()
                } else {
                    0f
                }

                val userEntity = UserEntity.findById(recipe.authorId)!!
                val user = userEntity.let { UserMapper.toDomain(it) }
                Triple(recipe, averageRating, user)
            }
        }
    }

    override suspend fun deleteRecipe(userId: Long, recipeId: Long): Recipe? {
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

    override suspend fun searchRecipeWithStr(str: String, page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page - 1) * limit
                val gson = Gson()

                val recipes = RecipeEntity.all().toList()

                val filteredRecipes = recipes.filter { recipeEntity ->
                    val recetteDetails: RecipeDetails = gson.fromJson(recipeEntity.recette, RecipeDetails::class.java)

                    val ingredientsMatch = recetteDetails.ingredients.map { ingredient ->
                        ingredient.lowercase().contains(str.lowercase())
                    }

                    ingredientsMatch.contains(true) || recipeEntity.title.lowercase().contains(str.lowercase()) ||
                            recipeEntity.description.lowercase().contains(str.lowercase())
                }

                filteredRecipes.drop(offset).take(limit).map { recipeEntity ->
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

    override suspend fun getByIngredients(ingredients: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page - 1) * limit
                val gson = Gson()

                val recipes = RecipeEntity.all().toList()

                val normalizedIngredients = ingredients.map { it.lowercase().trim() }

                val filteredRecipes = recipes.filter { recipeEntity ->
                    val recetteDetails: RecipeDetails = gson.fromJson(recipeEntity.recette, RecipeDetails::class.java)

                    val recetteIngredients = recetteDetails.ingredients.map { it.lowercase().trim() }

                    normalizedIngredients.all { reqIngredient ->
                        recetteIngredients.any { recetteIngredient ->
                            recetteIngredient.contains(reqIngredient)
                        }
                    }
                }

                filteredRecipes.drop(offset).take(limit).map { recipeEntity ->
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


    fun parseTags(tagsString: String): List<String> {
        return tagsString
            .replace("[", "")
            .replace("]", "")
            .replace("'", "")
            .split(",")
            .map { it.trim().lowercase() }
    }

    override suspend fun getByFilters(
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
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page - 1) * limit
                val gson = Gson()

                var conditions: Op<Boolean> = (RecipeTable.difficulty lessEq difficulty)

                if (nbPersons.size == 2) {
                    conditions = conditions and (RecipeTable.nbPersons lessEq nbPersons[1]) and
                            (RecipeTable.nbPersons greaterEq nbPersons[0])
                }

                if (preparationTime.size == 2) {
                    conditions = conditions and (RecipeTable.preparationTime lessEq preparationTime[1]) and
                            (RecipeTable.preparationTime greaterEq preparationTime[0])
                }

                val recipes = RecipeEntity.find {
                    conditions
                }.toList()

                val filteredRecipes = recipes.filter { recipeEntity ->
                    val recetteDetails: RecipeDetails = gson.fromJson(recipeEntity.recette, RecipeDetails::class.java)

                    val ingredientsMatch = recetteDetails.ingredients.map { it.lowercase() }
                        .none { it in exclusions.map { exclusion -> exclusion.lowercase() } }

                    val tagsMatch = (recipeEntity.tags).map { it.lowercase().replace("\"", "") }
                        .containsAll(tags.map { it.lowercase() })

                    ingredientsMatch && tagsMatch
                }

                val filteredByListsRecipes = filteredRecipes.map { recipeEntity ->
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

                val sorted = if (order == "desc") {
                    filteredByListsRecipes.sortedWith(comparator.reversed())
                } else {
                    filteredByListsRecipes.sortedWith(comparator)
                }

                sorted.drop(offset).take(limit)


            }
        }
    }

    override suspend fun getByTagsAny(tags: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page - 1) * limit


                val recipes = RecipeEntity.all().toList()

                val filteredRecipes = recipes.filter { recipeEntity ->

                    val tagsMatch = parseTags(recipeEntity.tags).map { it.lowercase().replace("\"", "") }
                        .any { it in tags.map { tag -> tag.lowercase() } }

                    tagsMatch
                }

                filteredRecipes.drop(offset).take(limit).map { recipeEntity ->
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

    override suspend fun getByTagsAll(tags: List<String>, page: Int, limit: Int): List<Pair<Recipe, Float>> {
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page - 1) * limit


                val recipes = RecipeEntity.all().toList()

                val filteredRecipes = recipes.filter { recipeEntity ->

                    val tagsMatch = parseTags(recipeEntity.tags).map { it.lowercase().replace("\"", "") }
                        .containsAll(tags.map { it.lowercase() })

                    tagsMatch
                }

                filteredRecipes.drop(offset).take(limit).map { recipeEntity ->
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
        return withContext(Dispatchers.IO) {
            transaction {
                val offset = (page - 1) * limit
                val gson = Gson()

                var conditions: Op<Boolean> = (RecipeTable.difficulty lessEq difficulty)

                if (nbPersons.size == 2) {
                    conditions = conditions and (RecipeTable.nbPersons lessEq nbPersons[1]) and
                            (RecipeTable.nbPersons greaterEq nbPersons[0])
                }

                if (preparationTime.size == 2) {
                    conditions = conditions and (RecipeTable.preparationTime lessEq preparationTime[1]) and
                            (RecipeTable.preparationTime greaterEq preparationTime[0])
                }

                val recipes = RecipeEntity.find { conditions }.toList()

                val filteredRecipes = recipes.filter { recipeEntity ->
                    val recetteDetails: RecipeDetails = gson.fromJson(recipeEntity.recette, RecipeDetails::class.java)

                    val ingredientsMatch = recetteDetails.ingredients.any { ingredient ->
                        ingredient.lowercase().contains(query.lowercase())
                    }
                    val titleMatch = recipeEntity.title.lowercase().contains(query.lowercase())
                    val descriptionMatch = recipeEntity.description.lowercase().contains(query.lowercase())

                    ingredientsMatch || titleMatch || descriptionMatch
                }

                val filteredByListsRecipes = filteredRecipes.filter { recipeEntity ->
                    val recetteDetails: RecipeDetails = gson.fromJson(recipeEntity.recette, RecipeDetails::class.java)

                    val ingredientsMatch = recetteDetails.ingredients.map { it.lowercase() }
                        .none { it in exclusions.map { exclusion -> exclusion.lowercase() } }

                    val tagsMatch = parseTags(recipeEntity.tags).map { it.lowercase().replace("\"", "") }
                        .containsAll(tags.map { it.lowercase() })

                    ingredientsMatch && tagsMatch
                }

                val recipesWithRatings = filteredByListsRecipes.map { recipeEntity ->
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

                val sortedRecipes = if (order == "desc") {
                    recipesWithRatings.sortedWith(comparator.reversed())
                } else {
                    recipesWithRatings.sortedWith(comparator)
                }

                sortedRecipes.drop(offset).take(limit)
            }
        }
    }
    override suspend fun getRecipeByIdByUserSession(recipeId: Long, userId: Long): Pair<Recipe, Float>? {
        return withContext(Dispatchers.IO){
            transaction {
                // Trouver la recette par son ID
                val recipeEntity = RecipeEntity.findById(recipeId)

                // Si la recette n'existe pas, retourner null
                if (recipeEntity == null) {
                    return@transaction null
                }

                // Mapper l'entité recette vers le domaine
                val recipe = RecipeMapper.toDomain(recipeEntity)

                // Vérifier si l'userId correspond à l'authorId de la recette
                if (recipe.authorId != userId) {
                    return@transaction null // Retourne null si l'utilisateur n'est pas l'auteur
                }

                // Récupérer les notes pour la recette
                val ratings = RecipeRatingsEntity.find { RecipeRatingsTable.recipeId eq recipe.id }.toList()
                val averageRating = if (ratings.isNotEmpty()) {
                    ratings.map { it.rating }.average().toFloat()
                } else {
                    0f
                }

                recipe to averageRating // Retourner la recette avec sa note
            }
        }
    }


}