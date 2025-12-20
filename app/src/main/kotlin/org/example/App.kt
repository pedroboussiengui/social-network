package org.example

import ch.qos.logback.classic.LoggerContext
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.example.infra.database.exposed.Migrations
import org.example.infra.database.postgres.DatabaseConnection
import org.example.infra.di.repositoryModule
import org.example.infra.di.useCaseModule
import org.example.infra.http.*
import org.koin.ktor.plugin.Koin
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

//todo: profile avatar image download
//todo: cache for like in post
//todo: jwt authentication
//todo: image upload for post with image or video

fun main(args: Array<String>) {
    val ctx = LoggerFactory.getILoggerFactory() as LoggerContext
    ctx.getLogger("org.jetbrains.exposed").level = ch.qos.logback.classic.Level.INFO
    ctx.getLogger("org.jetbrains.exposed.sql").level = ch.qos.logback.classic.Level.INFO
    ctx.getLogger("Exposed").level = ch.qos.logback.classic.Level.INFO
    ctx.getLogger("io.ktor").level = ch.qos.logback.classic.Level.INFO
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseConnection.init(environment.config)
//    Migrations.dropAll()
    Migrations.createAll()
    install(Koin) {
        modules(
            repositoryModule, useCaseModule
        )
    }
    install(ContentNegotiation) {
        json(Json {
            explicitNulls = false
        })
    }
    install(CallLogging) {
        level = Level.INFO
        filter { call -> true }
    }
    configureStatusPage()
    configureRequestValidation()
    helloModule()
    profileModule()
    postModule()
}