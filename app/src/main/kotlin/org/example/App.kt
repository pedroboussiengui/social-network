package org.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import org.example.infra.database.postgres.DatabaseConnection
import org.example.infra.database.exposed.Migrations
import org.example.infra.di.repositoryModule
import org.example.infra.di.useCaseModule
import org.example.infra.http.configureStatusPage

import org.example.infra.http.helloModule
import org.example.infra.http.postModule
import org.example.infra.http.profileModule
import org.koin.ktor.plugin.Koin

//todo: business validation in comment a post
//todo: comments with pagination [ok]
//todo: status page
//todo: request validation
//todo: cache for like in post
//todo: profile avatar image upload
//todo: image upload for post with image or video
//todo: jwt authentication

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
    configureStatusPage()
    helloModule()
    profileModule()
    postModule()
}