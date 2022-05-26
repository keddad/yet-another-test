package com.testproject.models

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Who needs databases anyway?

@Serializable
data class BookMeta(var name: String, var author: String)

@Serializable
data class CustomerBook(var amount: Int, var book: BookMeta)

@Serializable
data class ShopBook(var price: Int, var amount: Int, var id: Int, var book: BookMeta)

@Serializable
data class JsonBook(var price: Int, var amount: Int, var id: Int, var name: String, var author: String)

@Serializable
data class JsonAccount(var money: Int)

@Serializable
data class Account(var balance: Int, @EncodeDefault var books: MutableList<CustomerBook> = mutableListOf())

@Serializable
data class Shop(var books: MutableList<ShopBook>)

@Serializable
data class LogEntity(var time: Long, var text: String)

@Serializable
data class MarketDealRequest(var id: Int, var amount: Int)

@Serializable
data class JsonContainer(var books: List<JsonBook>, var account: JsonAccount)
