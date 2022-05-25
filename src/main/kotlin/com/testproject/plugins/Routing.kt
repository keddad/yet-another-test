package com.testproject.plugins

import com.testproject.models.Account
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import com.testproject.models.Shop
import com.testproject.logging.defaultLogger

fun Application.configureRouting(shop: Shop, account: Account, logger: (String) -> Unit = ::defaultLogger) {

    routing {
        get("/account") {
            logger("Hey, I'm an account GET call!")
            call.respond(account)
        }

        get("/market") {
            logger("Hey, I'm a market GET call!")
            call.respond(shop)
        }

        get("/meow") {
            account.balance += 200
        }
    }
}
