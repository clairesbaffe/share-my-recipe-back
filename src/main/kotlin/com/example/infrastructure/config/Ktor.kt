package com.example.infrastructure.config

import com.example.infrastructure.model.UserSession
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.sessions.*
import org.koin.ktor.plugin.Koin
import org.koin.ksp.generated.defaultModule

fun Application.configureKtor(config: ApplicationConfig) {
    install(Koin) {
        modules(defaultModule)
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            writerWithDefaultPrettyPrinter()
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }

    val secretKey = config.property("ktor.sessions.secretKey").getString()

    install(Sessions) {
        cookie<UserSession>("USER_SESSION") {
            cookie.path = "/"
            cookie.httpOnly = true
            cookie.secure = false
            cookie.maxAgeInSeconds = 3600

            transform(SessionTransportTransformerMessageAuthentication(secretKey.toByteArray()))
        }
    }

    configureRouting()
    configureExceptionHandling()

    Database.init(config)

    environment.monitor.subscribe(ApplicationStopping) {
        Database.closeConnections()
    }
}