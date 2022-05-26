package com.testproject

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.testproject.plugins.*

class ApplicationTest {
    @Test
    fun testNewMarket() = testApplication {
        var (shop, account) = loadDb("data.json")

        application {
            configureRouting(shop, account)
            configureSerialization()
        }
        client.get("/market").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                "{\"books\":[{\"price\":1000,\"amount\":7,\"id\":0,\"book\":{\"name\":\"Совершенный код\",\"author\":\"Стив Макконелл\"}},{\"price\":1500,\"amount\":15,\"id\":1,\"book\":{\"name\":\"Философия Java\",\"author\":\"Брюс Эккель\"}},{\"price\":25000,\"amount\":10,\"id\":2,\"book\":{\"name\":\"Effective Java\",\"author\":\"Joshua Bloch\"}}]}",
                bodyAsText()
            )
        }
    }

    @Test
    fun testNewAccount() = testApplication {
        var (shop, account) = loadDb("data.json")

        application {
            configureRouting(shop, account)
            configureSerialization()
        }
        client.get("/account").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("{\"balance\":20000,\"books\":[]}", bodyAsText())
        }
    }

    @Test
    fun testInvalidAmount() = testApplication {
        var (shop, account) = loadDb("data.json")

        application {
            configureRouting(shop, account)
            configureSerialization()
        }
        client.post("/market/deal") {
            setBody("""{"id":2,"amount":9}""")
            header("Content-Type", ContentType.Application.Json.toString())
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    @Test
    fun testInvalidCount() = testApplication {
        var (shop, account) = loadDb("data.json")

        application {
            configureRouting(shop, account)
            configureSerialization()
        }
        client.post("/market/deal") {
            setBody("""{"id":0,"amount":8}""")
            header("Content-Type", ContentType.Application.Json.toString())
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    @Test
    fun testValidTransaction() = testApplication {
        var (shop, account) = loadDb("data.json")

        application {
            configureRouting(shop, account)
            configureSerialization()
        }
        client.post("/market/deal") {
            setBody("""{"id":0,"amount":2}""")
            header("Content-Type", ContentType.Application.Json.toString())
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        client.get("/market").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                "{\"books\":[{\"price\":1000,\"amount\":5,\"id\":0,\"book\":{\"name\":\"Совершенный код\",\"author\":\"Стив Макконелл\"}},{\"price\":1500,\"amount\":15,\"id\":1,\"book\":{\"name\":\"Философия Java\",\"author\":\"Брюс Эккель\"}},{\"price\":25000,\"amount\":10,\"id\":2,\"book\":{\"name\":\"Effective Java\",\"author\":\"Joshua Bloch\"}}]}",
                bodyAsText()
            )
        }

        client.get("/account").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                "{\"balance\":13000,\"books\":[{\"amount\":2,\"book\":{\"name\":\"Совершенный код\",\"author\":\"Стив Макконелл\"}}]}",
                bodyAsText()
            )
        }
    }


}