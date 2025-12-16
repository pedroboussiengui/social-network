package org.example

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.example.infra.database.postgres.DatabaseConnection
import org.example.infra.database.exposed.Migrations
import org.example.infra.di.repositoryModule
import org.example.infra.di.useCaseModule

import org.example.infra.http.helloModule
import org.example.infra.http.postModule
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    DatabaseConnection.init(environment.config)
    Migrations.run()
    install(Koin) {
        modules(
            repositoryModule, useCaseModule
        )
    }
    install(ContentNegotiation) {
        json()
    }
    helloModule()
    postModule()
}