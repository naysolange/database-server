package com.rc

import com.rc.http.HttpServer
import com.rc.core.infrastructure.InMemoryDatabase

fun main() {
   val database = InMemoryDatabase()
   HttpServer(database).start()
}
