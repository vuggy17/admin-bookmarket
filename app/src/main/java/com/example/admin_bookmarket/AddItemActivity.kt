package com.example.admin_bookmarket

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.admin_bookmarket.ViewModel.AddItemViewModel
import com.example.admin_bookmarket.databinding.ActivityAddItemBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class AddItemActivity : AppCompatActivity() {

    //set up fire storage

    private val reference: StorageReference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    lateinit var binding: ActivityAddItemBinding
    private var newBook: MutableMap<String, Any> = mutableMapOf()
    private val viewModel: AddItemViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idAddBook.setOnClickListener {
            //addNewBook()
            pushImageToStorage()
        }
        binding.idImgeURL.addTextChangedListener {
            binding.idImgeURL.text.toString()?.let { uri -> loadImageFromUri(Uri.parse(uri)) }
        }

        binding.idBack.setOnClickListener {
            onBackPressed()
        }
        binding.idThumbnail.setOnClickListener {
            openGallery()
        }
    }

    private fun pushImageToStorage(){
        if(imageUri != null){
            val fileRef: StorageReference = reference.child(System.currentTimeMillis().toString() + "." + getFileExtension(imageUri as Uri))
            fileRef.putFile(imageUri as Uri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {
                    binding.idImgeURL.setText(it.toString())
                    addNewBook()
                    Toast.makeText(this, "Upload success image", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this, "Upload failed image", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getFileExtension(uri: Uri): String?{
        var cr: ContentResolver = contentResolver
        var mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }
    private fun openGallery() {
        val galleryIntent: Intent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            binding.idThumbnail.setImageURI(imageUri)
            Glide
                .with(baseContext)
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.idTnBackground)
        }
    }

    private fun addNewBook() {
        if (binding.idCount.text.toString() != "" && binding.idImgeURL.text.toString() != "" && binding.idAuthor.text.toString() != "" &&
            binding.idTitle.text.toString() != "" && binding.idDescription.text.toString() != "" && binding.idPrice.text.toString() != "" &&
            binding.idKind.text.toString() != ""
        ) {
            newBook = mutableMapOf(
                "Image" to binding.idImgeURL.text.toString(),
                "Name" to binding.idTitle.text.toString(),
                "Author" to binding.idAuthor.text.toString(),
                "Price" to binding.idPrice.text.toString().toDouble().roundToInt(),
                "rate" to "0".toDouble().roundToInt(),
                "Kind" to binding.idKind.text.toString(),
                "Counts" to binding.idCount.text.toString().toDouble().roundToInt(),
                "Description" to binding.idDescription.text.toString()
            )

            viewModel.addtoDb(newBook)
            binding.idCount.setText("", TextView.BufferType.EDITABLE)
            binding.idImgeURL.setText("", TextView.BufferType.EDITABLE)
            binding.idAuthor.setText("", TextView.BufferType.EDITABLE)
            binding.idTitle.setText("", TextView.BufferType.EDITABLE)
            binding.idDescription.setText("", TextView.BufferType.EDITABLE)
            binding.idPrice.setText("", TextView.BufferType.EDITABLE)
            binding.idKind.setText("", TextView.BufferType.EDITABLE)
        }
        Toast.makeText(this, "Add success", Toast.LENGTH_SHORT).show()
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