package com.testproject

import com.testproject.logging.defaultLogger
import com.testproject.logging.getFileLogger
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.testproject.plugins.*
import com.testproject.models.Account
import com.testproject.models.Shop
import kotlin.reflect.KFunction1

fun main(args: Array<String>) {

    assert(args.size == 1) { "No data.json" }

    var (shop, account) = loadDb(args[0])
    var logger = ::defaultLogger

    if (args.size == 2) {
        logger = getFileLogger(args[1]) as KFunction1<String, Unit>
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting(shop, account, logger)
        configureSerialization()

    }.start(wait = true)
}
