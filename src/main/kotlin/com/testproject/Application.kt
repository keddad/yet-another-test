package com.testproject

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.testproject.plugins.*
import com.testproject.models.Account
import com.testproject.models.Shop

fun main(args: Array<String>) {

    assert(args.size == 1) { "No data.json" }

    var (shop, account) = loadDb(args[0])

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting(shop, account)
        configureSerialization()
    }.start(wait = true)
}
