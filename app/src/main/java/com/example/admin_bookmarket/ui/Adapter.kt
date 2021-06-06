package com.example.admin_bookmarket.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_bookmarket.RecyclerViewClickListener
import com.example.admin_bookmarket.databinding.ItemRcBinding
import com.example.admin_bookmarket.model.BookDetailItem
import com.google.android.material.card.MaterialCardView

class Adapter(
    private val values: List<BookDetailItem>, private val itemListener:RecyclerViewClickListener
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemRcBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            (this as MaterialCardView).setOnLongClickListener{
                isChecked = !isChecked
                return@setOnLongClickListener true
            }
            this.checkedIcon = null

            setOnClickListener {
                itemListener.recyclerViewListClicked(this, values[position].id)
            }
        }

        val item = values[position]
        holder.bookName.text = item.name
        holder.bookQuantity.text = item.quantity.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding:ItemRcBinding) : RecyclerView.ViewHolder(binding.root) {
        val bookName = binding.ircName
        val bookQuantity = binding.ircNumbers

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

}