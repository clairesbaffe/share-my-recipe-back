package com.example.infrastructure.mapper

import com.example.domain.model.User
import com.example.infrastructure.adapter.output.entity.UserEntity

object UserMapper {
    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id.value,
            username = entity.username,
            passwordHash = entity.passwordHash,
            roles = entity.roles.split(","),
            date = entity.date
        )
    }

    fun toEntity(user: User): UserEntity {
        return UserEntity.new {
            username = user.username
            passwordHash = user.passwordHash
            roles = user.roles.joinToString(",")
            date = user.date
        }
    }
}