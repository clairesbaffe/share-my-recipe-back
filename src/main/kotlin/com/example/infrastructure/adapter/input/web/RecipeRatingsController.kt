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
    post("/{id}/ratings") {
        try {
            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")

            val recipeRequest = call.receive<RecipeRating>()
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("Invalid or missing recipe ID")

            recipeRatingUseCase.postRating(userSession.userId, recipeId, recipeRequest.rating)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Rating submitted successfully"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Error while submitting rating")
        }
    }

    get("/{id}/ratings"){
        try{
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")

            val ratings = recipeRatingUseCase.getRatingsForRecipe(recipeId)
            call.respond(HttpStatusCode.OK, ratings)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }

    get("/{id}/ratings/overall"){
        try{
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")

            val overall = recipeRatingUseCase.getOverallRatingForRecipe(recipeId)
            call.respond(HttpStatusCode.OK, overall)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }
}