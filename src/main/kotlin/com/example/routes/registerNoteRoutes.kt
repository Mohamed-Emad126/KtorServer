package com.example.routes

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerNoteRoutes(){
    routing {
        notesRouting()
        imagesRouting()
    }
}