package com.example.infrastructure.adapter.output.entity

import com.example.infrastructure.adapter.output.entity.RecipeRatingsTable.references
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date

object RecipeTable : LongIdTable() {
    val title = varchar("title", 255)
    val image = text("image")
    val description = text("description")
    val recette = text("recette")
    val preparationTime = float("preparation_time")
    val nbPersons = integer("nb_persons")
    val difficulty = float("difficulty")
    val tags = text("tags")
    val authorId = long("author_id").references(UserTable.id)
    val date = date("date")
}

class RecipeEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RecipeEntity>(RecipeTable)

    var title by RecipeTable.title
    var image by RecipeTable.image
    var description by RecipeTable.description
    var recette by RecipeTable.recette
    var preparationTime by RecipeTable.preparationTime
    var nbPersons by RecipeTable.nbPersons
    var difficulty by RecipeTable.difficulty
    var tags by RecipeTable.tags
    var authorId by RecipeTable.authorId
    var date by RecipeTable.date
}

