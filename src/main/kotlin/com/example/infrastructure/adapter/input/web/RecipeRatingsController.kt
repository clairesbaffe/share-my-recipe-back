package com.example.infrastructure.adapter.input.web

import com.example.application.port.input.RecipeRatingsUseCasePort
import com.example.application.port.input.RecipeUseCasePort
import com.example.infrastructure.adapter.input.web.dto.RecipeRating
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.recipeRatingsController() {
    val recipeRatingUseCase: RecipeRatingsUseCasePort by inject()

    post("/{id}/ratings"){
        try{
            val recipeRequest = call.receive<RecipeRating>()
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")
            recipeRatingUseCase.postRating(recipeRequest.userId, recipeId, recipeRequest.rating)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Recette créée avec succès"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }
}