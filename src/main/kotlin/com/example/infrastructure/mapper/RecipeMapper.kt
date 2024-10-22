package com.example.infrastructure.mapper

import com.example.domain.model.Recipe
import com.example.infrastructure.adapter.output.entity.RecipeEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object RecipeMapper {
    private val gson = Gson()

    fun toDomain(entity: RecipeEntity): Recipe {
        val tagsList: List<String> = gson.fromJson(entity.tags, object : TypeToken<List<String>>() {}.type)

        return Recipe(
            id = entity.id.value,
            title = entity.title,
            image = entity.image,
            description = entity.description,
            recette = entity.recette,
            preparationTime = entity.preparationTime,
            nbPersons = entity.nbPersons,
            difficulty = entity.difficulty,
            tags = tagsList,
            authorId = entity.authorId,
            date = entity.date
        )
    }

    fun toEntity(recipe: Recipe): RecipeEntity {
        val tagsJson: String = gson.toJson(recipe.tags)

        return RecipeEntity.new {
            title = recipe.title
            image = recipe.image
            description = recipe.description
            recette = recipe.recette
            preparationTime = recipe.preparationTime
            nbPersons = recipe.nbPersons
            difficulty = recipe.difficulty
            tags = tagsJson
            authorId = recipe.authorId
            date = recipe.date
        }
    }

}