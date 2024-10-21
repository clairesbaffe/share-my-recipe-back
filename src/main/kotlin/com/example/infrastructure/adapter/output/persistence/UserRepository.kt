package com.example.infrastructure.adapter.output.persistence

import com.example.application.port.output.UserRepositoryPort
import com.example.domain.model.User
import com.example.infrastructure.adapter.output.entity.UserEntity
import com.example.infrastructure.adapter.output.entity.UserTable
import com.example.infrastructure.mapper.UserMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single

@Single
class UserRepository : UserRepositoryPort {

    override suspend fun findByUsername(username: String): User? {
        return withContext(Dispatchers.IO) {
            transaction {
                UserEntity.find { UserTable.username eq username }.singleOrNull()?.let {
                    UserMapper.toDomain(it)
                }
            }
        }
    }

    override suspend fun save(user: User): User {
        return withContext(Dispatchers.IO) {
            transaction {
                val entity = UserEntity.new {
                    username = user.username
                    passwordHash = user.passwordHash
                    roles = user.roles.joinToString(",")
                }
                UserMapper.toDomain(entity)
            }
        }
    }
}