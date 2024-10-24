package com.example.infrastructure.adapter.input.web

import com.example.application.port.input.RecipeUseCasePort
import com.example.infrastructure.adapter.input.web.dto.*
import com.example.infrastructure.exception.RecipeNotFound
import com.example.infrastructure.model.UserSession
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import java.time.LocalDate

fun Route.publicRecipeController() {
    val recipeUseCase: RecipeUseCasePort by inject()
    val gson = Gson()

    get("/{id}") {
        try {
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID recette invalide ou manquant")

            val recipeFound = recipeUseCase.findRecipeById(recipeId)
                ?: throw RecipeNotFound("La recette $recipeId introuvable")

            val (recipe, rating) = recipeUseCase.getRecipeByIdWithRate(recipeFound)
                ?: throw RecipeNotFound("La recette $recipeId introuvable")

            val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)

            val recipeResponseDTO = RecipeWithRatingResponseDTO(
                id = recipe.id,
                title = recipe.title,
                image = recipe.image,
                description = recipe.description,
                recette = recetteDetails,
                preparationTime = recipe.preparationTime,
                nbPersons = recipe.nbPersons,
                difficulty = recipe.difficulty,
                tags = recipe.tags,
                authorId = recipe.authorId,
                date = recipe.date,
                rating = rating
            )

            call.respond(HttpStatusCode.OK, recipeResponseDTO)
        } catch (e: RecipeNotFound) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la récupération de la recette")
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la récupération de la recette")
        }
    }

    get("") {
        val limit = call.parameters["limit"]?.toIntOrNull()
            ?: 20
        val page = call.parameters["page"]?.toIntOrNull()
            ?: 1
        val recipesWithRatings = recipeUseCase.getRecipeWithRate(page, limit)
        val recipesDTO = recipesWithRatings.map { (recipe, rating) ->
            val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)

            RecipeWithRatingResponseDTO(
                id = recipe.id,
                title = recipe.title,
                image = recipe.image,
                description = recipe.description,
                recette = recetteDetails,
                preparationTime = recipe.preparationTime,
                nbPersons = recipe.nbPersons,
                difficulty = recipe.difficulty,
                tags = recipe.tags,
                authorId = recipe.authorId,
                date = recipe.date,
                rating = rating
            )
        }

        call.respond(HttpStatusCode.OK, recipesDTO)
    }

    get("/ordered") {
        val order = call.parameters["order"] ?: "asc"
        val sortedBy = call.parameters["sortedBy"] ?: "ratings"
        val limit = call.parameters["limit"]?.toIntOrNull()
            ?: 20
        val page = call.parameters["page"]?.toIntOrNull()
            ?: 1
        val recipesWithRatings = recipeUseCase.getRecipeOrderBy(order, sortedBy, page, limit)

        val recipesDTO = recipesWithRatings.map { (recipe, rating) ->
            val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)

            RecipeWithRatingResponseDTO(
                id = recipe.id,
                title = recipe.title,
                image = recipe.image,
                description = recipe.description,
                recette = recetteDetails,
                preparationTime = recipe.preparationTime,
                nbPersons = recipe.nbPersons,
                difficulty = recipe.difficulty,
                tags = recipe.tags,
                authorId = recipe.authorId,
                date = recipe.date,
                rating = rating
            )
        }
        call.respond(HttpStatusCode.OK, recipesDTO)
    }

    get("/users/{userId}"){
        try{
            val limit = call.parameters["limit"]?.toIntOrNull()
                ?: 20
            val page = call.parameters["page"]?.toIntOrNull()
                ?: 1
            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID utilisateur invalide ou manquant")
            val recipesWithRatings = recipeUseCase.getRecipeByUser(userId, page, limit)
            val recipesDTO = recipesWithRatings.map { (recipe, rating, user) ->
                val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)

                RecipeWithRatingAndUsernameResponseDTO(
                    id = recipe.id,
                    title = recipe.title,
                    image = recipe.image,
                    description = recipe.description,
                    recette = recetteDetails,
                    preparationTime = recipe.preparationTime,
                    nbPersons = recipe.nbPersons,
                    difficulty = recipe.difficulty,
                    tags = recipe.tags,
                    authorId = user.id,
                    authorName = user.username,
                    date = recipe.date,
                    rating = rating
                )
            }
            call.respond(HttpStatusCode.OK, recipesDTO)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                e.message ?: "Erreur lors de la récupération des recettes de l'utilisateur"
            )
        }
    }

    get("/search"){
        val recipeRequest = call.receive<SearchRecipes>()
        val limit = call.parameters["limit"]?.toIntOrNull()
            ?: 20
        val page = call.parameters["page"]?.toIntOrNull()
            ?: 1

        val searched = recipeUseCase.searchRecipeWithStr(recipeRequest.search, page, limit)
        val recipesDTO = searched.map { (recipe, rating) ->
            val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)

            RecipeWithRatingResponseDTO(
                id = recipe.id,
                title = recipe.title,
                image = recipe.image,
                description = recipe.description,
                recette = recetteDetails,
                preparationTime = recipe.preparationTime,
                nbPersons = recipe.nbPersons,
                difficulty = recipe.difficulty,
                tags = recipe.tags,
                authorId = recipe.authorId,
                date = recipe.date,
                rating = rating
            )
        }
        call.respond(HttpStatusCode.OK, recipesDTO)
    }

    get("/search/ingredients"){
        val recipeRequest = call.receive<SearchRecipesByIngredients>()
        val limit = call.parameters["limit"]?.toIntOrNull()
            ?: 20
        val page = call.parameters["page"]?.toIntOrNull()
            ?: 1

        val searched = recipeUseCase.getByIngredients(recipeRequest.search, page, limit)
        val recipesDTO = searched.map { (recipe, rating) ->
            val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)

            RecipeWithRatingResponseDTO(
                id = recipe.id,
                title = recipe.title,
                image = recipe.image,
                description = recipe.description,
                recette = recetteDetails,
                preparationTime = recipe.preparationTime,
                nbPersons = recipe.nbPersons,
                difficulty = recipe.difficulty,
                tags = recipe.tags,
                authorId = recipe.authorId,
                date = recipe.date,
                rating = rating
            )
        }
        call.respond(HttpStatusCode.OK, recipesDTO)
    }
}



fun Route.recipeController() {
    val recipeUseCase: RecipeUseCasePort by inject()
    val gson = Gson()

    get("/users") {
        try {
            val limit = call.parameters["limit"]?.toIntOrNull()
                ?: 20
            val page = call.parameters["page"]?.toIntOrNull()
                ?: 1
            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")
            val recipesWithRatings = recipeUseCase.getRecipeByUser(userSession.userId, page, limit)
            val recipesDTO = recipesWithRatings.map { (recipe, rating) ->
                val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)

                RecipeWithRatingResponseDTO(
                    id = recipe.id,
                    title = recipe.title,
                    image = recipe.image,
                    description = recipe.description,
                    recette = recetteDetails,
                    preparationTime = recipe.preparationTime,
                    nbPersons = recipe.nbPersons,
                    difficulty = recipe.difficulty,
                    tags = recipe.tags,
                    authorId = recipe.authorId,
                    date = recipe.date,
                    rating = rating
                )
            }
            call.respond(HttpStatusCode.OK, recipesDTO)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                e.message ?: "Erreur lors de la récupération des recettes de l'utilisateur actuel"
            )
        }
    }

    post("") {
        val recipeRequest = call.receive<RecipeRequest>()
        try {
            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")
            val recetteJson = gson.toJson(recipeRequest.recette)

            recipeUseCase.postRecipe(
                title = recipeRequest.title,
                image = recipeRequest.image,
                description = recipeRequest.description,
                recette = recetteJson,
                preparationTime = recipeRequest.preparationTime,
                nbPersons = recipeRequest.nbPersons,
                difficulty = recipeRequest.difficulty,
                tags = recipeRequest.tags,
                authorId = userSession.userId,
                date = LocalDate.now()
            )
            call.respond(HttpStatusCode.Created, mapOf("message" to "Recette créée avec succès"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }

    delete("/{recipeId}/{userId}") {
        try {
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID recette invalide ou manquant")

            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID utilisateur invalide ou manquant")

            recipeUseCase.deleteRecipe(userId, recipeId)
            call.respond(HttpStatusCode.Created, mapOf("message" to "La recette a bien été supprimée"))
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                e.message ?: "Erreur lors de la suppression de la recette de l'utilisateur"
            )
        }
    }

    delete("/{recipeId}") {
        try {
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID recette invalide ou manquant")

            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")

            recipeUseCase.deleteRecipe(userSession.userId, recipeId)
            call.respond(HttpStatusCode.Created, mapOf("message" to "La recette a bien été supprimée"))
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                e.message ?: "Erreur lors de la suppression de la recette de l'utilisateur"
            )
        }
    }


}