package com.testproject.logging

import com.testproject.models.JsonContainer
import com.testproject.models.LogEntity
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

// Обычные логгеры для Ktor настраиваются через XML на этапе компиляции, насколько я понял, так что я произвел костыли

fun defaultLogger(s: String) {
    println(Json.encodeToString(LogEntity.serializer(), LogEntity(System.currentTimeMillis(), s)))
}

fun getFileLogger(path: String): (String) -> Unit {
    fun fileLogger(s: String) {
        val json = if (File(path).exists()) File(path).readText() else "[]"
        val cont = Json.decodeFromString<MutableList<LogEntity>>(json)
        cont.add(LogEntity(System.currentTimeMillis(), s))
        File(path).writeText(Json.encodeToString(ListSerializer(LogEntity.serializer()), cont))
    }

    return ::fileLogger
}