package com.testproject.plugins

import com.testproject.models.Account
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import com.testproject.models.Shop

fun Application.configureRouting(shop: Shop, account: Account) {

    routing {
        get("/account") {
            call.respond(account)
        }

        get("/market") {
            call.respond(shop)
        }

        get("/meow") {
            account.balance += 200
        }
    }
}
