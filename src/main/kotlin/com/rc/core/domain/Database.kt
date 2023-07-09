package com.rc.core.domain

interface Database {
    fun get(key: String) : String
    fun set(key: String, value: String)
}
