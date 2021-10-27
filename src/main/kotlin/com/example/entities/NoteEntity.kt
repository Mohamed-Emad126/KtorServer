package com.example.entities

import me.liuwj.ktorm.schema.*

object NoteEntity: Table<Nothing>("notes"){
    val id = int("note_id").primaryKey()
    val title = varchar("note_title")
    val content = text("note_content")
    val image = varchar("image")
}