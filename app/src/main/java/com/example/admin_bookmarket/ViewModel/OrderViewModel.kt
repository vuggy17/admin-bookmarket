package com.example.admin_bookmarket.ViewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.data.OrderRepository
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Cart
import com.example.admin_bookmarket.data.model.MyUser
import com.example.admin_bookmarket.data.model.Order
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    @ApplicationContext    private val appContext: Context
) :
    ViewModel() {

    private var _orders = MutableLiveData<MutableList<Order>>()
    private var allOrderValue: MutableList<Order> = ArrayList()
    private var _membersID = MutableLiveData<MutableList<String>>()
    val orders get() = _orders


    private fun setBillingItem(userID: String, orderID: String) {
        orderRepository.getAllBillingIemFromDB(userId = userID, orderID)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    if(!AppUtils.checkInternet(context = appContext)){
                        Toast.makeText(appContext,"Please checking your internet connection!", Toast.LENGTH_SHORT).show()
                    }
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
                    for (order in _orders.value!!) {
                        if (order.id == orderID) {
                            order.listbooks = billingList
                        }
                        orderList.add(order)
                    }
                    _orders.value = orderList
                }
            }
    }

    fun getAllOrder(): MutableLiveData<MutableList<Order>> {
        orderRepository.getAllUserFromDB().addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
                if(!AppUtils.checkInternet(context = appContext)){
                    Toast.makeText(appContext,"Please checking your internet connection!", Toast.LENGTH_SHORT).show()
                }
            } else {

                for (doc in value!!) {
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
                if(!AppUtils.checkInternet(context = appContext)){
                    Toast.makeText(appContext,"Please checking your internet connection!", Toast.LENGTH_SHORT).show()
                }
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
                    order.cancelReason = doc["reason"].toString()
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
                    setBillingItem(userId, orderID = order.id)
                    if (isExitsInAllOrder(order) == null) {
                        allOrderValue.add(order)
                    } else {
                        allOrderValue[allOrderValue.indexOf(isExitsInAllOrder(order))] = order
                    }
                    orders.value = allOrderValue
                }
            }
        }
    }

    private fun isExitsInAllOrder(newOrder: Order): Order? {
        for (order in allOrderValue) {
            if (order.id == newOrder.id) {
                return order
            }
        }
        return null
    }

    private fun getFormatDate(date: Date): String {
        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy ")
        return sdf.format(date)
    }

    fun updateUserStatus(userId: String, docId: String, status: String): Boolean {
        return if (orderRepository.updateOrderStatus(userId, docId, status)) {
            true
        }else{
            if(!AppUtils.checkInternet(context = appContext)){
                Toast.makeText(appContext,"Please checking your internet connection!", Toast.LENGTH_SHORT).show()
            }
            false
        }

    }
}