package com.example.infrastructure.mapper

import com.example.domain.model.Book
import com.example.domain.model.Recipe
import com.example.infrastructure.adapter.output.entity.BookEntity
import com.example.infrastructure.adapter.output.entity.RecipeEntity

object RecipeMapper {
    fun toDomain(entity: RecipeEntity): Recipe {
        return Recipe(
            entity.id.value,
            entity.title,
            entity.image,
            entity.description,
            entity.preparationTime,
            entity.nbPersons,
            entity.difficulty,
            entity.tags.split(","),
            entity.ratings,
            entity.authorId,
            entity.date
        )
    }

    fun toEntity(recipe: Recipe): RecipeEntity {
        return RecipeEntity.new {
            this.title = recipe.title
            this.image = recipe.image
            this.description = recipe.description
            this.preparationTime = recipe.preparationTime
            this.nbPersons = recipe.nbPersons
            this.difficulty = recipe.difficulty
            this.tags = recipe.tags.joinToString(",")  // Convert List<String> to a comma-separated String
            this.ratings = recipe.ratings
            this.authorId = recipe.authorId
            this.date = recipe.date
        }

    }
}