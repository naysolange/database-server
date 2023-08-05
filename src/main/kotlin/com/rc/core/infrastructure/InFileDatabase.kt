package com.rc.core.infrastructure

import com.rc.core.domain.Database
import com.rc.core.domain.exception.KeyNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

class InFileDatabase(private val filePath: String) : Database {
    private val map = mutableMapOf<String, String>()
    private lateinit var file: Path

    init {
        createFileIfNotExists()
        loadFileToMap()
    }

    override fun get(key: String): String {
        return map[key] ?: throw KeyNotFoundException("Key '$key' not found")
    }

    override fun set(key: String, value: String) {
        map[key] = value
        val lines = mutableListOf<String>()
        map.forEach { entry -> lines.add(entry.key + "=" + entry.value) }
        file.writeLines(lines)
    }

    private fun createFileIfNotExists() {
        val path = Paths.get(filePath)
        file = if (Files.exists(path)) path else Files.createFile(path)
    }

    private fun loadFileToMap() {
        val lines = file.readLines()
        lines.forEach {
            val line = it.split("=")
            val key = line[0]
            val value = line[1]
            map[key] = value
        }
    }
}
