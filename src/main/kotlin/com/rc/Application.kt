package com.rc

import com.rc.core.infrastructure.InFileDatabase
import com.rc.http.HttpServer

fun main() {
   val database = InFileDatabase("src/main/resources/database.txt")
   HttpServer(database).start()
}
