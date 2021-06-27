package com.example.admin_bookmarket.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.data.OrderRepository
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Cart
import com.example.admin_bookmarket.data.model.MyUser
import com.example.admin_bookmarket.data.model.Order
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class OrderViewModel@Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel()  {

    private var _order = MutableLiveData<MutableList<Order>>()
    val orders get() = _order

    private fun setBillingItem(userID: String, orderID: String) {
        orderRepository.getAllBillingIemFromDB(userId = userID, orderID).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                var billingList: ArrayList<Cart> = ArrayList()
                for (doc in value!!) {
                    var cart: Cart = Cart()
                    cart.numbers = doc["Quantity"].toString().toDouble().roundToInt()
                    cart.name = doc["title"].toString()
                    cart.price = doc["price"].toString().toLong()
                    cart.id = doc.id
                    billingList.add(cart)
                }
                var orderList: MutableList<Order> = ArrayList()
                for(order in _order.value!!){
                    if(order.id == orderID){
                        order.listbooks = billingList
                    }
                    orderList.add(order)
                }
                _order.value = orderList
            }
        }
    }



    private fun getAllOrderOfId(userId: String): MutableLiveData<MutableList<Order>> {
        orderRepository.getAllOrderFromDB(userId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                var orderList: MutableList<Order> = ArrayList()
                for (doc in value!!) {
                    val order = Order()
                    order.id = doc.id
                    val timeStamp = doc["dateTime"] as Timestamp
                    order.dateTime = getFormatDate(timeStamp.toDate())
                    order.status = doc["status"].toString()
                    order.totalPrince = doc["totalPrince"].toString() +" đ"
                    order.userDeliverAddress.addressLane = doc["addressLane"].toString()
                    order.userDeliverAddress.fullName = doc["fullName"].toString()
                    order.userDeliverAddress.phoneNumber = doc["phoneNumber"].toString()
                    order.userDeliverAddress.city = doc["city"].toString()
                    order.userDeliverAddress.district = doc["district"].toString()
                   //Get user here
                    setBillingItem( userId, orderID = order.id)
                    orderList.add(order)
                }
                orders.value = orderList
            }
        }
        return orders
    }
    private fun getFormatDate(date: Date):String{
        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy ")
        return sdf.format(date)
    }
}