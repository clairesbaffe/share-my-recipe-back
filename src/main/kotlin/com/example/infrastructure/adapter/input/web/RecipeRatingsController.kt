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
            val limit = call.parameters["limit"]?.toIntOrNull()
                ?: 20
            val page = call.parameters["page"]?.toIntOrNull()
                ?: 1

            val ratings = recipeRatingUseCase.getRatingsForRecipe(recipeId, page, limit)
            call.respond(HttpStatusCode.OK, ratings)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la récupération des notes la recette")
        }
    }

    get("/{recipeId}/overall"){
        try{
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")

            val overall = recipeRatingUseCase.getOverallRatingForRecipe(recipeId)
            call.respond(HttpStatusCode.OK, overall)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors du calcul de note globale de le recette")
        }
    }

    get("{recipeId}/{userId}"){
        try{
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")
            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID utilisateur invalide ou manquant")

            val ratingByUser = recipeRatingUseCase.getRatingsForRecipeByUser(userId, recipeId)
            call.respond(HttpStatusCode.OK, ratingByUser)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la récupération des notes")
        }
    }

    get("{recipeId}"){
        try{
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")
            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")

            val ratingByUser = recipeRatingUseCase.getRatingsForRecipeByUser(userSession.userId, recipeId)
            call.respond(HttpStatusCode.OK, ratingByUser)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la récupération des notes")
        }
    }

    get("/users/{userId}"){
        try{
            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID utilisateur invalide ou manquant")
            val limit = call.parameters["limit"]?.toIntOrNull()
                ?: 20
            val page = call.parameters["page"]?.toIntOrNull()
                ?: 1

            val ratingByUser = recipeRatingUseCase.getRatingsByUser(userId, page, limit)
            call.respond(HttpStatusCode.OK, ratingByUser)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la récupération des notes effectuées par l'utilisateur")
        }
    }

    get("/users"){
        try{
            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")
            val limit = call.parameters["limit"]?.toIntOrNull()
                ?: 20
            val page = call.parameters["page"]?.toIntOrNull()
                ?: 1

            val ratingByUser = recipeRatingUseCase.getRatingsByUser(userSession.userId, page, limit)
            call.respond(HttpStatusCode.OK, ratingByUser)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la récupération des notes effectuées par l'utilisateur")
        }
    }

    get(""){
        try{
            val limit = call.parameters["limit"]?.toIntOrNull()
                ?: 20
            val page = call.parameters["page"]?.toIntOrNull()
                ?: 1
            val allRates = recipeRatingUseCase.getAllRates(page, limit)
            call.respond(HttpStatusCode.OK, allRates)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la récupération des notes")
        }
    }

    delete("/{recipeId}/{userId}" ){
        try {
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID recette invalide ou manquant")

            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID utilisateur invalide ou manquant")

            recipeRatingUseCase.deleteRating(recipeId, userId)
            call.respond(HttpStatusCode.Created, mapOf("message" to "La note $recipeId a bien été supprimée"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la suppression de note")
        }
    }

    delete("/{recipeId}" ){
        try {
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID recette invalide ou manquant")

            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")

            recipeRatingUseCase.deleteRating(userSession.userId, recipeId)
            call.respond(HttpStatusCode.Created, mapOf("message" to "La note $recipeId a bien été supprimée"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la suppression de note")
        }
    }

    patch("/{recipeId}/{userId}") {
        try{
            val recipeRequest = call.receive<RecipeRating>()
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID recette invalide ou manquant")

            val userId = call.parameters["userId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID utilisateur invalide ou manquant")

            recipeRatingUseCase.patchRating(userId, recipeId, recipeRequest.rating)
            call.respond(HttpStatusCode.Created, mapOf("message" to "La note $recipeId a bien été modifiée"))
        }catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la modification de note")
        }
    }

    patch("/{recipeId}") {
        try{
            val recipeRequest = call.receive<RecipeRating>()
            val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID recette invalide ou manquant")

            val userSession = call.sessions.get<UserSession>()
                ?: throw IllegalArgumentException("User not logged in or session expired")

            recipeRatingUseCase.patchRating(userSession.userId, recipeId, recipeRequest.rating)
            call.respond(HttpStatusCode.Created, mapOf("message" to "La note $recipeId a bien été modifiée"))
        }catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la suppression de note")
        }
    }
}