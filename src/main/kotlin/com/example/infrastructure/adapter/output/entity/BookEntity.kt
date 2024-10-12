package com.example.infrastructure.adapter.output.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object BookTable : LongIdTable() {
    val title = varchar("title", 255)
    val author = varchar("author", 255)
}

class BookEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<BookEntity>(BookTable)

    var title by BookTable.title
    var author by BookTable.author
}