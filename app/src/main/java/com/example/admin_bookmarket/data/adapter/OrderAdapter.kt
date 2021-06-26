package com.example.admin_bookmarket.data.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_bookmarket.R
import com.example.admin_bookmarket.data.model.Order
import java.text.DecimalFormat

class OrderAdapter(
    var listOder: MutableList<Order>,
    var context: Context
): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val status: TextView = view.findViewById(R.id.status)
        val dateTime: TextView = view.findViewById(R.id.dateTime)
        val name: TextView = view.findViewById(R.id.orderName)
        val address: TextView = view.findViewById(R.id.orderAddress)
        val phone: TextView = view.findViewById(R.id.orderPhoneNumber)
        val listItemOrder: RecyclerView = view.findViewById(R.id.orderItemBill)
        val totalPrice: TextView = view.findViewById(R.id.orderSum)
        val expandAddress: Button = view.findViewById(R.id.expandAddress)
        val expandBill: Button = view.findViewById(R.id.expandBill)
        val addressLayout: LinearLayout = view.findViewById(R.id.addressLayout)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return ViewHolder(view)
    }
    fun addOrder(change: MutableList<Order>){
        if (this.listOder.isNotEmpty()) {
            this.listOder.clear()
        }
        this.listOder.addAll(change)
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentOrder: Order = listOder[position]
        holder.apply {
            val formatter = DecimalFormat("#,###")
            status.text = currentOrder.status
            name.text = currentOrder.userDeliverAddress.fullName
            phone.text = currentOrder.userDeliverAddress.phoneNumber
            address.text = currentOrder.userDeliverAddress.addressLane +", "+ currentOrder.userDeliverAddress.district +", "+ currentOrder.userDeliverAddress.city+"."
            dateTime.text = currentOrder.dateTime
            totalPrice.text = formatter.format(currentOrder.totalPrince.toLong()) + "đ"
            val billingItemAdapter = BillingItemAdapter(currentOrder.listbooks)
            listItemOrder.adapter = billingItemAdapter
            listItemOrder.layoutManager = LinearLayoutManager(context)
            listItemOrder.visibility = View.GONE
            addressLayout.visibility = View.GONE
            expandAddress.setOnClickListener {
                onExpandAddressClick(addressLayout, expandAddress)
            }
            expandBill.setOnClickListener {
                onExpandBillClick(listItemOrder, expandBill)
            }


        }
    }

    private fun onExpandBillClick(listItemOrder: RecyclerView, expandButton: Button){
        if(listItemOrder.visibility == View.GONE){
            listItemOrder.visibility = View.VISIBLE
            expandButton.setBackgroundResource(R.drawable.ic_baseline_expand_less_24)

        }else{
            listItemOrder.visibility = View.GONE
            expandButton.setBackgroundResource(R.drawable.ic_baseline_expand_more_24)
        }
    }

    private fun onExpandAddressClick(addressLayout: LinearLayout, expandButton: Button){
        if(addressLayout.visibility == View.GONE){
            addressLayout.visibility = View.VISIBLE
            expandButton.setBackgroundResource(R.drawable.ic_baseline_expand_less_24)

        }else{
            addressLayout.visibility = View.GONE
            expandButton.setBackgroundResource(R.drawable.ic_baseline_expand_more_24)
        }
    }

    override fun getItemCount(): Int {
        return listOder.size
    }


}