package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val title: String? = null,
    val content: String,
    val image: String? = null,
)