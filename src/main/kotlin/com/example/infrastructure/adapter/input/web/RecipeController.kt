package com.example.infrastructure.adapter.input.web

import com.example.application.port.input.RecipeRatingsUseCasePort
import com.example.application.port.input.RecipeUseCasePort
import com.example.infrastructure.adapter.input.web.dto.RecipeRequest
import com.example.infrastructure.adapter.input.web.dto.RecipeResponseDTO
import com.example.infrastructure.adapter.input.web.dto.RecipeDetails
import com.example.infrastructure.adapter.input.web.dto.RecipeRating
import com.example.infrastructure.exception.RecipeNotFound
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDate

fun Route.recipeController() {
    val recipeUseCase: RecipeUseCasePort by inject()
    val gson = Gson()

    get("/{id}") {
        val recipeId = call.parameters["id"]?.toLongOrNull()
            ?: throw IllegalArgumentException("ID de recette invalide ou manquant")

        val recipe = recipeUseCase.findRecipeById(recipeId)
            ?: throw RecipeNotFound("Recette non trouvée")

        val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)

        val recipeDTO = RecipeResponseDTO(
            id = recipe.id,
            title = recipe.title,
            image = recipe.image,
            description = recipe.description,
            recette = recetteDetails,
            preparationTime = recipe.preparationTime,
            nbPersons = recipe.nbPersons,
            difficulty = recipe.difficulty,
            tags = recipe.tags,
            ratings = recipe.ratings,
            authorId = recipe.authorId,
            date = recipe.date
        )
        call.respond(HttpStatusCode.OK, recipeDTO)
    }

    get("") {
        val recipes = recipeUseCase.findAllRecipe()
        val recipesDTO = recipes.map { recipe ->
            val recetteDetails: RecipeDetails = gson.fromJson(recipe.recette, RecipeDetails::class.java)

            RecipeResponseDTO(
                id = recipe.id,
                title = recipe.title,
                image = recipe.image,
                description = recipe.description,
                recette = recetteDetails,
                preparationTime = recipe.preparationTime,
                nbPersons = recipe.nbPersons,
                difficulty = recipe.difficulty,
                tags = recipe.tags,
                ratings = recipe.ratings,
                authorId = recipe.authorId,
                date = recipe.date
            )
        }
        call.respond(HttpStatusCode.OK, recipesDTO)
    }

    post("") {
        val recipeRequest = call.receive<RecipeRequest>()
        try {
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
                ratings = recipeRequest.ratings,
                authorId = recipeRequest.authorId,
                date = LocalDate.now()
            )
            call.respond(HttpStatusCode.Created, mapOf("message" to "Recette créée avec succès"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }

    patch ("/{id}/ratings"){
        try{
            val recipeRequest = call.receive<RecipeRating>()
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")

            recipeUseCase.updateRecipeRating(recipeId, recipeRequest.rating)
            call.respond(HttpStatusCode.Created, mapOf("message" to "Recette créée avec succès"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }

    }

    get("/{id}/ratings"){
        try{
            val recipeRequest = call.receive<RecipeRating>()
            val recipeId = call.parameters["id"]?.toLongOrNull()
                ?: throw IllegalArgumentException("ID de recette invalide ou manquant")

            val ratings = recipeUseCase.getRatingsForRecipe(recipeId)
            call.respond(HttpStatusCode.OK, ratings)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Erreur lors de la création de la recette")
        }
    }
}