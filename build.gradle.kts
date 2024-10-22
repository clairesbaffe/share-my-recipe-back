val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version = "3.2.0"
val kotlin_logging_version = "2.0.11"
val koin_ksp_version = "1.0.1"

plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.jpa") version "1.8.20"
    id("org.liquibase.gradle") version "2.1.1"
    id("io.ktor.plugin") version "2.3.4"
    id("com.google.devtools.ksp") version "1.8.20-1.0.11"
    application
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.20")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.20")

    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlin_logging_version")

    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")
    implementation("org.postgresql:postgresql:42.7.2")

    implementation("org.liquibase:liquibase-core:4.23.0")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")

    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("org.mindrot:jbcrypt:0.4")

    implementation("com.google.code.gson:gson:2.11.0")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")
}