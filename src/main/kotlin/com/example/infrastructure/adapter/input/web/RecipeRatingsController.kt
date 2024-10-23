package com.example.infrastructure.adapter.input.web

import com.example.application.port.input.RecipeRatingsUseCasePort
import com.example.infrastructure.adapter.input.web.dto.RecipeRating
import com.example.infrastructure.model.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject


fun Route.recipeRatingsController() {
    val recipeRatingUseCase: RecipeRatingsUseCasePort by inject()

    get("/recipe/{recipeId}"){
        try{
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")

            val ratings = recipeRatingUseCase.getRatingsForRecipe(recipeId)
            call.respond(HttpStatusCode.OK, ratings)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }

    get("/{id}/overall"){
        try{
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")

            val overall = recipeRatingUseCase.getOverallRatingForRecipe(recipeId)
            call.respond(HttpStatusCode.OK, overall)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }

    get("{id}/{userId}"){
        try{
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")
            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID utilisateur invalide ou manquant")

            val ratingByUser = recipeRatingUseCase.getRatingsForRecipeByUser(userId, recipeId)
            call.respond(HttpStatusCode.OK, ratingByUser)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }

    get("/users/{userId}"){
        try{
            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID utilisateur invalide ou manquant")

            val ratingByUser = recipeRatingUseCase.getRatingsByUser(userId)
            call.respond(HttpStatusCode.OK, ratingByUser)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }

    get("/users"){
        try{
            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")

            val ratingByUser = recipeRatingUseCase.getRatingsByUser(userSession.userId)
            call.respond(HttpStatusCode.OK, ratingByUser)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }

    get(""){
        try{
            val allRates = recipeRatingUseCase.getAllRates()
            call.respond(HttpStatusCode.OK, allRates)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }

    delete("/{recipeId}/{userId}" ){
        try {
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid or missing recipe ID")

            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid or missing user ID")

            recipeRatingUseCase.deleteRating(recipeId, userId)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Rating deleted successfully"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Error while submitting rating")
        }
    }

    delete("/{recipeId}" ){
        try {
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid or missing recipe ID")

            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")

            recipeRatingUseCase.deleteRating(userSession.userId, recipeId)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Rating deleted successfully"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Error while submitting rating")
        }
    }

    patch("/{recipeId}/{userId}") {
        try{
            val recipeRequest = call.receive<RecipeRating>()
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid or missing recipe ID")

            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid or missing user ID")

            recipeRatingUseCase.patchRating(userId, recipeId, recipeRequest.rating)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Rating modified successfully"))
        }catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Error while patching rating")
        }
    }

    patch("/{recipeId}") {
        try{
            val recipeRequest = call.receive<RecipeRating>()
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid or missing recipe ID")

            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")

            recipeRatingUseCase.patchRating(userSession.userId, recipeId, recipeRequest.rating)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Rating modified successfully"))
        }catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Error while patching rating")
        }
    }
}