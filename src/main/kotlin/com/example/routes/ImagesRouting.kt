package com.example.routes

import com.example.database.DatabaseConnection
import com.example.models.NoteResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

fun Route.imagesRouting() {
    val db = DatabaseConnection.db

    route("/images") {

        get("/d/{image_download}") {
            val imageId = call.parameters["image_download"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                NoteResponse(
                    data = "Missing or malformed id",
                    success = false
                )
            )
            val file = File("images/${imageId}.jpg")
            if (!file.exists()) {
                return@get call.respond(
                    status = HttpStatusCode.BadRequest,
                    NoteResponse(
                        data = "No Image with name: $imageId",
                        success = false
                    )
                )
            }

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(
                    ContentDisposition.Parameters.FileName, "${imageId}.jpg"
                ).toString()
            )

            call.respondFile(file)
        }

        get("/l/{image_open}") {
            val imageId = call.parameters["image_open"] ?: call.respond(
                status = HttpStatusCode.BadRequest,
                NoteResponse(
                    data = "Missing or malformed id",
                    success = false
                )
            )
            val file = File("images/${imageId}")
            if (!file.exists()) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    NoteResponse(
                        data = "No Image with name: $imageId",
                        success = false
                    )
                )
            }

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(
                    ContentDisposition.Parameters.FileName, "${imageId}.jpg"
                ).toString()
            )

            call.respondFile(file)
        }
    }
}