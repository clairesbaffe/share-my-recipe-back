package com.example.infrastructure.adapter.output.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object RecipeRatingsTable : LongIdTable() {
    val userId = long("userId")
    val recipeId = long("recipeId")
    val rating = float("rating")
}

class RecipeRatingsEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RecipeRatingsEntity>(RecipeRatingsTable)
    var userId by RecipeRatingsTable.userId
    var recipeId by RecipeRatingsTable.recipeId
    var rating by RecipeRatingsTable.rating

}