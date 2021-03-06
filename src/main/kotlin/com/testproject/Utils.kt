package com.testproject

import com.testproject.models.*
import java.io.File

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

fun loadDb(path: String): Pair<Shop, Account> {
    val json = File(path).readText()
    val cont = Json.decodeFromString<JsonContainer>(json)

    val shop = Shop(cont.books.map { ShopBook(it.price, it.amount, it.id, BookMeta(it.name, it.author)) } as MutableList<ShopBook>)
    val account = Account(cont.account.money)
    return Pair(shop, account)
}