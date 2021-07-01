package com.example.admin_bookmarket


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.admin_bookmarket.ViewModel.ItemDetailViewModel
import com.example.admin_bookmarket.data.model.Book
import com.example.admin_bookmarket.databinding.ActivityItemDetailBinding
import com.example.admin_bookmarket.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


@AndroidEntryPoint
class ItemDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityItemDetailBinding
    private val viewModel: ItemDetailViewModel by viewModels()
    private var displayItem: Book = Book()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.book.observe(this, changeObserver)

        setupOnlickListener()
        binding.idImgeURL.addTextChangedListener {
            binding.idImgeURL.text.toString()?.let { uri -> loadImageFromUri(Uri.parse(uri)) }
        }

        binding.idDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@ItemDetailActivity)
            builder.setMessage("Are you sure you want to DELETE this book?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                   deleteCurrentBook()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    private fun deleteCurrentBook()
    {
        viewModel.deleteBook(displayItem)
        startActivity(Intent(baseContext, MainActivity::class.java))
    }

    private val changeObserver = Observer<Book> { value ->
        value?.let{
            displayItem = value
            binding.idAuthor.setText( value.Author, TextView.BufferType.EDITABLE)
            binding.idPrice.setText( value.Price.toString() , TextView.BufferType.EDITABLE)
            binding.idRate.setText(value.rate.toString(), TextView.BufferType.EDITABLE)
            binding.idTitle.setText(value.Name, TextView.BufferType.EDITABLE)
            binding.idImgeURL.setText(value.Image, TextView.BufferType.EDITABLE)
            binding.idKind.setText(value.Kind, TextView.BufferType.EDITABLE)
            binding.idCount.setText(value.Counts.toString(), TextView.BufferType.EDITABLE)
            binding.idDescription.setText(value.Description, TextView.BufferType.EDITABLE)
            it.Image?.let { uri -> loadImageFromUri(Uri.parse(uri)) } }
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
    fun setupOnlickListener(){
        binding.idBack.setOnClickListener { onBackPressed() }
        binding.idUpdate.setOnClickListener { updateToDb() }
    }

    fun updateToDb()
    {
        var newBook = Book (displayItem.id, binding.idImgeURL.text.toString(), binding.idTitle.text.toString(), binding.idAuthor.text.toString(),
        binding.idPrice.text.toString().toDouble().roundToInt(), binding.idRate.text.toString().toDouble().roundToInt(), binding.idKind.text.toString(),
            binding.idCount.text.toString().toDouble().roundToInt(), binding.idDescription.text.toString())

        if (newBook != displayItem)
        {
            viewModel.updateToDb(displayItem.id!!, newBook)
        }
    }
//    private fun AskOption(): android.app.AlertDialog? {
//        return android.app.AlertDialog.Builder(this) // set message, title, and icon
//            .setTitle("Delete")
//            .setMessage("Do you want to Delete")
//            .setIcon(R.drawable.delete)
//            .setPositiveButton("Delete",
//                DialogInterface.OnClickListener { dialog, whichButton ->
//                    deleteCurrentBook()
//                    dialog.dismiss()
//                })
//            .setNegativeButton("cancel",
//                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
//            .create()
//    }
}