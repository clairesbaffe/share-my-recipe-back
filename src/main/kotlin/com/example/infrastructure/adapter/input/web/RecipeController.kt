package com.example.infrastructure.adapter.input.web

import com.example.infrastructure.exception.BookNotFound
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import com.example.infrastructure.adapter.input.web.dto.BookResponseDTO
import com.example.application.port.input.BookUseCasePort
import com.example.application.port.input.RecipeUseCasePort
import com.example.infrastructure.adapter.input.web.dto.RecipeResponseDTO
import com.example.infrastructure.exception.RecipeNotFound
import io.ktor.server.request.*
import org.koin.ktor.ext.inject
import java.time.LocalDate

fun Route.recipeController() {
    val recipeUseCase: RecipeUseCasePort by inject()

    // Request and Response data classes
    data class RecipeRequest(
        val title: String,
        val image: String,
        val description: String,
        val recette: String,
        val preparationTime: Float,
        val nbPersons: Int,
        val difficulty: Float,
        val tags: List<String>,
        val ratings: Float,
        val authorId: Long,
        val date: LocalDate  // Use LocalDate instead of Date
    )

    data class RecipeResponse(val message: String)

    // GET: Find recipe by ID
    get("/{id}") {
        val recipeId = call.parameters["id"]?.toLongOrNull()
            ?: throw IllegalArgumentException("ID de recette invalide ou manquant")

        val recipe = recipeUseCase.findRecipeById(recipeId)
            ?: throw RecipeNotFound("Recette non trouvée")

        val recipeDTO = RecipeResponseDTO(
            id = recipe.id,
            title = recipe.title,
            image = recipe.image,
            description = recipe.description,  // Fixed: use correct field
            recette = recipe.recette,
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

    // GET: Find all recipes
    get("/") {
        val recipes = recipeUseCase.findAllRecipe()
        val recipesDTO = recipes.map { recipe ->
            RecipeResponseDTO(
                id = recipe.id,
                title = recipe.title,
                image = recipe.image,
                description = recipe.description,  // Fixed: use correct field
                recette = recipe.recette,
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

    // POST: Create a new recipe
    post("/") {  // Fixed the route to match the others
        val recipeRequest = call.receive<RecipeRequest>()
        try {
            recipeUseCase.postRecipe(
                recipeRequest.title,
                recipeRequest.image,
                recipeRequest.description,
                recipeRequest.recette,
                recipeRequest.preparationTime,
                recipeRequest.nbPersons,
                recipeRequest.difficulty,
                recipeRequest.tags,
                recipeRequest.ratings,
                recipeRequest.authorId,
            )
            call.respond(HttpStatusCode.Created, RecipeResponse("Recette créée avec succès"))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "Erreur lors de la création de la recette")
        }
    }
}
