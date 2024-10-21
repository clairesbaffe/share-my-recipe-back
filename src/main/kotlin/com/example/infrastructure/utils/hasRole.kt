package com.example.infrastructure.utils

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun ApplicationCall.hasRole(role: String): Boolean {
    val principal = this.principal<JWTPrincipal>()
    val roles = principal?.payload?.getClaim("roles")?.asList(String::class.java) ?: emptyList()
    return role in roles
}