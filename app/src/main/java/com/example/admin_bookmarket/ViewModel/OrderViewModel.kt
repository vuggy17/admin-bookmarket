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
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@HiltViewModel
class OrderViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel()  {

    private var _orders = MutableLiveData<MutableList<Order>>()
    private var allOrderValue: MutableList<Order> = ArrayList()
    private var _membersID = MutableLiveData<MutableList<String>>()
    val orders get() = _orders



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
                for(order in _orders.value!!){
                    if(order.id == orderID){
                        order.listbooks = billingList
                    }
                    orderList.add(order)
                }
                _orders.value = orderList
            }
        }
    }

    fun getAllOrder(): MutableLiveData<MutableList<Order>>{
        orderRepository.getAllUserFromDB().addSnapshotListener { value, error ->
            if(error != null){
                Log.w(Constants.VMTAG, "Listen failed.", error)
            }else{

                for(doc in value!!){
                    getAllOrderOfId(doc.id)
                }

            }
        }
        return orders
    }

     private fun getAllOrderOfId(userId: String) {
        orderRepository.getAllOrderFromDB(userId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
            } else {
                for (doc in value!!) {
                    val order = Order()
                    order.id = doc.id
                    val timeStamp = doc["dateTime"] as Timestamp
                    order.dateTime = getFormatDate(timeStamp.toDate())
                    order.status = doc["status"].toString()
                    order.totalPrince = doc["totalPrince"].toString()
                    order.userDeliverAddress.addressLane = doc["addressLane"].toString()
                    order.userDeliverAddress.fullName = doc["fullName"].toString()
                    order.userDeliverAddress.phoneNumber = doc["phoneNumber"].toString()
                    order.userDeliverAddress.city = doc["city"].toString()
                    order.userDeliverAddress.district = doc["district"].toString()
                    val userMap = doc["user"] as HashMap<*, *>
                    order.currentUser = MyUser(
                        fullName = userMap["fullName"].toString(),
                        gender = userMap["gender"].toString(),
                        birthDay = userMap["birthDay"].toString(),
                        phoneNumber = userMap["phoneNumber"].toString(),
                        addressLane = userMap["addressLane"].toString(),
                        city = userMap["city"].toString(),
                        district = userMap["district"].toString(),
                    )
                    order.currentUser.email = doc["userId"].toString()
                    setBillingItem( userId, orderID = order.id)
                    isExitsInAllOrder(order)
                    allOrderValue.add(order)

                }
                orders.value = allOrderValue
            }
        }
    }
    private fun isExitsInAllOrder(newOrder: Order): Boolean{
        for(order in allOrderValue){
            if(order.id == newOrder.id){
                allOrderValue.remove(order)
                return true
            }
        }
        return false
    }

    private fun getFormatDate(date: Date):String{
        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy ")
        return sdf.format(date)
    }
}