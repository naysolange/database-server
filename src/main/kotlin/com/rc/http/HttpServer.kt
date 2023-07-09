package com.rc.http

import com.rc.core.domain.Database
import com.rc.core.domain.exception.BusinessException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class HttpServer(private val database: Database) {

    private val logger: Logger = LoggerFactory.getLogger(HttpServer::class.java)

    fun start() {
        embeddedServer(Netty, port = 4000) {
            main()
        }.start(wait = true)
    }

    private fun Application.main() {
        logger.info("Configuring endpoints..")
        configureRouting()
    }

    private fun Application.configureRouting() {
        routing {
            get("/get") {
                getValue()
            }
            post("/set") {
                setValue()
            }
        }
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.setValue() {
        val parameters = call.request.queryString().split("=")
        if (parameters.size == 2) {
            val key = parameters[0]
            val value = parameters[1]
            database.set(key, value)
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.BadRequest, "parameters are missing or invalid")
        }
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.getValue() {
        val key = call.request.queryParameters["key"]
        if (key.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "key parameter is missing")
        } else {
            try {
                val value = database.get(key)
                call.respondText(value)
            } catch(t: BusinessException) {
                call.respond(HttpStatusCode.BadRequest, t.localizedMessage)
            }
        }
    }
}
