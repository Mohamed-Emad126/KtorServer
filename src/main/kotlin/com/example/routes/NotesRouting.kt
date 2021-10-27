package com.example.routes

import com.example.database.DatabaseConnection
import com.example.entities.NoteEntity
import com.example.models.Note
import com.example.models.NoteResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.date.*
import me.liuwj.ktorm.dsl.*
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists

fun Application.notesRouting() {
    val db = DatabaseConnection.db
    routing {
        get("/notes") {
            val notes = db.from(NoteEntity).select()
                .map {
                    val id = it[NoteEntity.id]
                    val title = it[NoteEntity.title]
                    val content = it[NoteEntity.content]
                    val image = it[NoteEntity.image]
                    Note(id ?: -1, title ?: "", content ?: "", image ?: "")
                }
            call.respond(
                NoteResponse(
                    data = notes,
                    success = true
                )
            )
        }

        route("/note") {

            post {
                val multipartData = call.receiveMultipart()
                var title: String? = null
                var content = ""
                var image: String? = null
                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            if (part.name == "title") {
                                title = part.value
                            }
                            if (part.name == "content") {
                                content = part.value
                            }
                        }
                        is PartData.FileItem -> {
                            image = getTimeMillis().toString()
                            val fileBytes = part.streamProvider().readBytes()
                            File("images/$image.JPG").writeBytes(fileBytes)
                        }
                    }
                }
                val effectedRaws = db.insert(NoteEntity) {
                    if (!title.isNullOrBlank()) {
                        set(NoteEntity.title, title)
                    }
                    if (!image.isNullOrBlank()) {
                        set(NoteEntity.image, "$image.JPG")
                    }
                    set(NoteEntity.content, content)
                }
                if (effectedRaws > 0) {
                    call.respond(
                        status = HttpStatusCode.Created, NoteResponse(
                            data = "Image with Note Inserted Successfully!!",
                            success = true
                        )
                    )
                } else {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        NoteResponse(
                            data = "can't save the Image!!",
                            success = false
                        )
                    )
                }
            }


            delete("/{id}") {
                val noteId = call.parameters["id"] ?: return@delete call.respond(
                    status = HttpStatusCode.BadRequest,
                    NoteResponse(
                        data = "Missing or malformed id",
                        success = false
                    )
                )
                var image: String? = null
                val imageName = db.from(NoteEntity).select(NoteEntity.image)
                    .where { NoteEntity.id eq noteId.toInt() }
                    .map {
                        image = it[NoteEntity.image]
                        image
                    }
                println(imageName.joinToString())
                val noDeleted = db.delete(NoteEntity) {
                    it.id eq noteId.toInt()
                }
                if (noDeleted > 0) {
                    call.respond(
                        status = HttpStatusCode.Accepted,
                        NoteResponse(
                            data = "Note removed Successfully!!",
                            success = true
                        )
                    )
                    runCatching {
                        Path("images/${imageName[0]}").deleteIfExists()
                    }

                } else {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        NoteResponse(
                            data = "No note with id: $noteId",
                            success = false
                        )
                    )
                }

            }


        }

    }
}

