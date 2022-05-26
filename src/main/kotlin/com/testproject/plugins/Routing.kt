package com.testproject.plugins

import com.testproject.models.Account
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import com.testproject.models.Shop
import com.testproject.logging.defaultLogger
import com.testproject.models.CustomerBook
import com.testproject.models.MarketDealRequest
import io.ktor.http.*
import io.ktor.server.request.*

fun Application.configureRouting(shop: Shop, account: Account, logger: (String) -> Unit = ::defaultLogger) {

    routing {
        get("/account") {
            logger("Hey, I'm an account GET call!")
            call.respond(account)
        }

        post("/market/deal") {
            logger("Hey, I'm a market deal GET call!")
            val purchaseRequest = call.receive<MarketDealRequest>()

            val book = shop.books.find { it.id == purchaseRequest.id }

            if (book == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (book.amount * book.price > account.balance) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (book.amount < purchaseRequest.amount) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            account.balance -= book!!.amount * book!!.price
            book.amount -= purchaseRequest.amount

            if (book.amount == 0) {
                shop.books.remove(book)
            }

            val boughtBook = account.books.find { it.book.author == book.book.author && it.book.name == book.book.name }

            if (boughtBook != null) {
                boughtBook.amount += purchaseRequest.amount
            } else {
                account.books.add(CustomerBook(purchaseRequest.amount, book.book))
            }


            call.respond(HttpStatusCode.OK)
        }

        get("/market") {
            logger("Hey, I'm a market GET call!")
            call.respond(shop)
        }
    }
}
