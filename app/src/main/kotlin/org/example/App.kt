package org.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.*
import org.example.infra.database.exposed.Migrations
import org.example.infra.database.postgres.DatabaseConnection
import org.example.infra.di.repositoryModule
import org.example.infra.di.useCaseModule
import org.example.infra.http.*
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level

//todo: comments with pagination [ok]
//todo: request validation [ok]
//todo: business validation in comment a post [ok]
//todo: status page with problem pattern
//todo: profile avatar image upload
//todo: cache for like in post
//todo: jwt authentication
//todo: image upload for post with image or video

fun main(args: Array<String>): Unit = EngineMain.main(args)

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
        json()
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