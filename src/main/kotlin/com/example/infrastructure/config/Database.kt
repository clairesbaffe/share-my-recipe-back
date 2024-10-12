package com.example.infrastructure.config

import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import java.sql.Connection
import java.sql.DriverManager

object Database {
    private lateinit var connection: Connection

    fun init(config: ApplicationConfig) {
        val dbUrl = config.property("ktor.database.url").getString()
        val dbUser = config.property("ktor.database.user").getString()
        val dbPassword = config.property("ktor.database.password").getString()

        Database.connect(
            url = dbUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )

        connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)
        val changelog = config.property("ktor.liquibase.changelog").getString()
        val liquibase = Liquibase(changelog, ClassLoaderResourceAccessor(), JdbcConnection(connection))
        liquibase.update("")
    }

    fun closeConnections() {
        if (::connection.isInitialized && !connection.isClosed) {
            connection.close()
        }
    }
}