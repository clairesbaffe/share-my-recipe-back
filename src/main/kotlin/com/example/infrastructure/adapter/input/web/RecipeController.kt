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

fun Route.recipeController() {
    val recipeUseCase: RecipeUseCasePort by inject()
    val gson = Gson()

    get("/{id}") {
        try{
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid or missing recipe ID")

            val recipeFound = recipeUseCase.findRecipeById(recipeId)
                ?: throw RecipeNotFound("The recipe with id $recipeId not found")

            val (recipe, rating) = recipeUseCase.getRecipeByIdWithRate(recipeFound)
                ?: throw RecipeNotFound("Recipe with id: $recipeId")


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
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }

    }

//    get("") {
//        val recipes = recipeUseCase.findAllRecipe()
//        val recipesDTO = recipes.map { recipe ->
//            val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)
//
//            RecipeResponseDTO(
//                id = recipe.id,
//                title = recipe.title,
//                image = recipe.image,
//                description = recipe.description,
//                recette = recetteDetails,
//                preparationTime = recipe.preparationTime,
//                nbPersons = recipe.nbPersons,
//                difficulty = recipe.difficulty,
//                tags = recipe.tags,
//                authorId = recipe.authorId,
//                date = recipe.date
//            )
//        }
//        call.respond(HttpStatusCode.OK, recipesDTO)
//    }

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

    get("") {
        val recipesWithRatings = recipeUseCase.getRecipeWithRate()

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
        val recipesWithRatings = recipeUseCase.getRecipeOrderBy(order, sortedBy)

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



}