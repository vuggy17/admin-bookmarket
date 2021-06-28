package com.example.admin_bookmarket.data.model

import com.google.firebase.firestore.auth.User

data class Order(
    var id: String ="",
    var userDeliverAddress: UserDeliverAddress = UserDeliverAddress(),
    var status: String ="",
    var totalPrince: String = "0",
    var dateTime: String ="",
    var listbooks: ArrayList<Cart> = ArrayList(),
    var currentUser: MyUser = MyUser(),
    var cancelReason: String =""

)