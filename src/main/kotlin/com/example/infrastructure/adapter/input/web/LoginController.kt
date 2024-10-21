package com.example.infrastructure.adapter.input.web

import com.example.application.port.input.UserAuthenticationUseCasePort
import com.example.domain.model.User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.koin.ktor.ext.inject
import io.ktor.server.config.*
import java.util.*

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String)

fun Route.loginController(config: ApplicationConfig) {
    val userAuthUseCase: UserAuthenticationUseCasePort by inject()
    val jwtIssuer = config.property("ktor.jwt.issuer").getString()
    val jwtSecret = config.property("ktor.jwt.privateKey").getString()
    val jwtAudience = config.property("ktor.jwt.audience").getString()

    post("/login") {
        val loginRequest = call.receive<LoginRequest>()
        val user = userAuthUseCase.authenticate(loginRequest.username, loginRequest.password)

        if (user != null) {
            val token = generateToken(user, jwtIssuer, jwtSecret, jwtAudience)
            call.respond(LoginResponse(token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Identifiants invalides")
        }
    }
}

fun generateToken(user: User, issuer: String, secret: String, audience: String): String {
    return JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userId", user.id.toString())
        .withClaim("username", user.username)
        .withArrayClaim("roles", user.roles.toTypedArray())
        .withExpiresAt(Date(System.currentTimeMillis() + 600_000))
        .sign(Algorithm.HMAC256(secret))
}