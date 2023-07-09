package com.rc.core.infrastructure

import com.rc.core.domain.Database
import com.rc.core.domain.exception.KeyNotFoundException

class InMemoryDatabase : Database {

    private val map = mutableMapOf<String, String>()

    override fun get(key: String): String {
        return map[key] ?: throw KeyNotFoundException("Key '$key' not found")
    }

    override fun set(key: String, value: String) {
        map[key] = value
    }
}
