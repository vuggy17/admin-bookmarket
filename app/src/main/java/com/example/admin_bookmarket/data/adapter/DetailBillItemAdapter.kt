package com.example.admin_bookmarket.data.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_bookmarket.data.model.Cart
import com.example.admin_bookmarket.databinding.BillingItemBinding
import com.example.admin_bookmarket.databinding.DetailBillItemBinding
import java.text.DecimalFormat

class DetailBillItemAdapter(
    var billingItem: ArrayList<Cart>
): RecyclerView.Adapter<DetailBillItemAdapter.ViewHolder>() {
    class ViewHolder(binding: DetailBillItemBinding): RecyclerView.ViewHolder(binding.root){
        val itemName = binding.itName
        val itemPrice = binding.itPrice
        val itemNumbers = binding.itNumbers
        val itemId = binding.itId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DetailBillItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }
    fun onChange(newItems: ArrayList<Cart>) {
        billingItem = newItems
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formatter = DecimalFormat("#,###")
        val itemPrinceText = formatter.format(billingItem[position].price * billingItem[position].numbers)
        holder.itemPrice.text =itemPrinceText +"đ"
        holder.itemId.text = "(ID: "+billingItem[position].id+")"
        holder.itemName.text = billingItem[position].name
        holder.itemNumbers.text = "x" + billingItem[position].numbers.toString()
        Log.d("modal", "created")
    }

    override fun getItemCount(): Int {
        return billingItem.size
    }
}