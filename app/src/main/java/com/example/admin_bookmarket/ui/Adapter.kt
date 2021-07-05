package com.example.admin_bookmarket.ui

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.example.admin_bookmarket.R
import com.example.admin_bookmarket.RecyclerViewClickListener
import com.example.admin_bookmarket.data.model.Book
import com.example.admin_bookmarket.databinding.CardBookBinding
import com.google.android.material.card.MaterialCardView

class Adapter(
    private var values: MutableList<Book>, private val itemListener:RecyclerViewClickListener
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            CardBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    fun onChange(newList:MutableList<Book>)
    {
        values = newList
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){

            setOnClickListener {
                itemListener.recyclerViewListClicked(this, values[position].id!!)
            }
        }

        val item = values[position]
        holder.bookName.text = item.Name
        holder.bookAuthor.text = item.Author
        holder.bookCount.text = item.rate.toString()
        holder.bookPrice.text = item.Price.toString() + " đ"
        Glide
            .with(holder.itemView)
            .asBitmap()
            .load(item.Image)
            .centerCrop()
            .placeholder(R.drawable.bg_white_placeholder)
            .transition(BitmapTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.bookImage);
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding:CardBookBinding) : RecyclerView.ViewHolder(binding.root) {
        val bookName = binding.bookTitle
        val bookCount = binding.bookRate
        val bookPrice = binding.bookPrice
        val bookAuthor = binding.bookAuthor
        val bookImage = binding.BookImage
//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

}