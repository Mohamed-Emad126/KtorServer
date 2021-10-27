package com.example

import com.example.database.DatabaseConnection
import com.example.entities.NoteEntity
import com.example.routes.notesRouting
import com.example.routes.registerNoteRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.liuwj.ktorm.dsl.insert

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        install(ContentNegotiation) {
            json()
        }
        registerNoteRoutes()
    }.start(wait = true)
}
