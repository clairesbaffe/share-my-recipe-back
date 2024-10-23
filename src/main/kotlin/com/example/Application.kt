package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.infrastructure.config.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.routing.*

fun main() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val host = config.property("ktor.deployment.host").getString()
    val port = config.property("ktor.deployment.port").getString().toInt()

    embeddedServer(Netty, port = port, host = host, configure = {
        requestReadTimeoutSeconds = 30
    }) {
        module(config)
    }.start(wait = true)
}

fun Application.module(config: ApplicationConfig) {
    configureKtor(config)
}