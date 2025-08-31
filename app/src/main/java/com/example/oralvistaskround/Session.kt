package com.example.oralvistaskround

import java.io.Serializable

data class Session(
    val sessionId: String,
    val name: String,
    val age: Int,
    val photoPaths: List<String>
) : Serializable
