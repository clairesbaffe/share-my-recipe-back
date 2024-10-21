package com.example.infrastructure.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
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
        }
    }

    install(Authentication) {
        jwt("auth-jwt") {
            val jwtAudience = config.property("ktor.jwt.audience").getString()
            val jwtIssuer = config.property("ktor.jwt.issuer").getString()
            val jwtRealm = config.property("ktor.jwt.realm").getString()
            val jwtSecret = config.property("ktor.jwt.privateKey").getString()

            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }

    configureRouting(config)
    configureExceptionHandling()

    Database.init(config)

    environment.monitor.subscribe(ApplicationStopping) {
        Database.closeConnections()
    }
}