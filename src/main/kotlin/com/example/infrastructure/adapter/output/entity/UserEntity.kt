package com.example.infrastructure.adapter.output.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date

object UserTable : LongIdTable("user") {
    val username = varchar("username", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 60)
    val roles = varchar("roles", 255)
    val date = date("date")
}

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UserTable)

    var username by UserTable.username
    var passwordHash by UserTable.passwordHash
    var roles by UserTable.roles
    var date by UserTable.date
}