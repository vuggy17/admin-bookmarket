package com.example.admin_bookmarket

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.admin_bookmarket.ViewModel.AddItemViewModel
import com.example.admin_bookmarket.databinding.ActivityAddItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddItemActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddItemBinding
    private var newBook: MutableMap<String, String> = mutableMapOf()
    private val viewModel: AddItemViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idAddBook.setOnClickListener {
            addNewBook()
        }
        binding.idImgeURL.addTextChangedListener {
            binding.idImgeURL.text.toString()?.let { uri -> loadImageFromUri(Uri.parse(uri)) }
        }

    }

    fun addNewBook()
    {
        if (binding.idCount.text.toString() != "" && binding.idImgeURL.text.toString() != "" && binding.idAuthor.text.toString() != "" &&
            binding.idTitle.text.toString() != "" && binding.idDescription.text.toString() != "" && binding.idPrice.text.toString() != "" &&
            binding.idKind.text.toString() != "")
        {
            newBook =  mutableMapOf("Image" to binding.idImgeURL.text.toString(), "Name" to binding.idTitle.text.toString(),
            "Author" to binding.idAuthor.text.toString(), "Price" to binding.idPrice.text.toString(), "rate" to "0",
                "Kind" to binding.idKind.text.toString(), "Counts" to binding.idCount.text.toString(), "Description" to binding.idDescription.text.toString())
            viewModel.addtoDb(newBook)
            binding.idCount.setText("", TextView.BufferType.EDITABLE)
            binding.idImgeURL.setText("", TextView.BufferType.EDITABLE)
            binding.idAuthor.setText("", TextView.BufferType.EDITABLE)
            binding.idTitle.setText("", TextView.BufferType.EDITABLE)
            binding.idDescription.setText("", TextView.BufferType.EDITABLE)
            binding.idPrice.setText("", TextView.BufferType.EDITABLE)
            binding.idKind.setText("", TextView.BufferType.EDITABLE)
        }
    }
    private fun loadImageFromUri(uri: Uri) {
        Glide
            .with(baseContext)
            .load(uri)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.idTnBackground)
        Glide
            .with(baseContext)
            .load(uri)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.idThumbnail)
    }
}