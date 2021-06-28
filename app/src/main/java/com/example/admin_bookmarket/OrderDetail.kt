package com.example.admin_bookmarket

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_bookmarket.data.adapter.DetailBillItemAdapter
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.model.Order
import com.example.admin_bookmarket.databinding.ActivityOrderDetailBinding
import com.example.admin_bookmarket.databinding.OrderItemBinding
import java.text.DecimalFormat

class OrderDetail : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setUpView(){
        val currentOrder: Order = AppUtils.currentOrder
        binding.apply {
            orderId.text = currentOrder.id
            status.setText(
                currentOrder.status,
                false
            )
            val formatter = DecimalFormat("#,###")
            userName.text = currentOrder.currentUser.fullName
            userEmail.text = currentOrder.currentUser.email
            userPhoneNumber.text =currentOrder.currentUser.phoneNumber
            dateTime.text = currentOrder.dateTime
            orderName.text = currentOrder.userDeliverAddress.fullName
            orderPhoneNumber.text = currentOrder.userDeliverAddress.phoneNumber
            orderAddress.text = currentOrder.userDeliverAddress.addressLane +", "+ currentOrder.userDeliverAddress.district +", "+ currentOrder.userDeliverAddress.city+"."
            val billAdapter: DetailBillItemAdapter = DetailBillItemAdapter(currentOrder.listbooks)
            orderItemBill.adapter = billAdapter
            orderItemBill.layoutManager = LinearLayoutManager(this.root.context)
            orderSum.text = formatter.format(currentOrder.totalPrince.toLong())+"đ"

        }

    }
}