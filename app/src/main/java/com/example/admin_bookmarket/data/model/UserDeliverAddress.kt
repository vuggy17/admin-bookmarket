package com.example.admin_bookmarket.data.model

data class UserDeliverAddress(var id:String = "",
                              var fullName: String="",
                              var phoneNumber: String="",
                              var addressLane: String="",
                              var district: String="",
                              var city: String= "",
                              var chose: Boolean = false)