package com.example.infrastructure.mapper

import com.example.domain.model.Recipe
import com.example.domain.model.RecipeRating
import com.example.infrastructure.adapter.output.entity.RecipeEntity
import com.example.infrastructure.adapter.output.entity.RecipeRatingsEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object RecipeRatingsMapper {
    private val gson = Gson()

    fun toDomain(entity: RecipeRatingsEntity): RecipeRating {
        return RecipeRating(
            userId = entity.userId,
            recipeId = entity.recipeId,
            rating = entity.rating,
        )
    }

    fun toEntity(recipeRating: RecipeRating): RecipeRatingsEntity {
        return RecipeRatingsEntity.new {
            userId = recipeRating.userId
            recipeId = recipeRating.recipeId
            rating = recipeRating.rating
        }
    }
}