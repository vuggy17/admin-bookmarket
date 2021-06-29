package com.example.admin_bookmarket.data.model

data class Cart(
    var id: String ="",
    var imgUrl: String ="",
    var name: String="",
    var author: String="",
    var numbers: Int=0,
    var price: Long=0,
    var isChose: Boolean=false,
)