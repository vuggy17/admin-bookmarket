package com.example.admin_bookmarket

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.admin_bookmarket.ViewModel.AddItemViewModel
import com.example.admin_bookmarket.databinding.ActivityAddItemBinding
import com.example.admin_bookmarket.ui.login.LoadDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class AddItemActivity : AppCompatActivity() {

    //set up fire storage

    private val reference: StorageReference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null
    private lateinit var loadDialog: LoadDialog
    lateinit var binding: ActivityAddItemBinding
    private var newBook: MutableMap<String, Any> = mutableMapOf()
    private val viewModel: AddItemViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(binding.root)

        binding.idAddBook.setOnClickListener {
            pushImageToStorage()
        }


        binding.idBack.setOnClickListener {
            onBackPressed()
        }
        binding.idThumbnail.setOnClickListener {
            openGallery()
        }
        val statusValue = resources.getStringArray(R.array.kind)
        val arrayAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.status_dropdown_item,
            statusValue
        )
        binding.idKind.setAdapter(arrayAdapter)

    }

    private fun checkValidEditText(editText: EditText): Boolean {
        return if (editText.text.isBlank()) {
            editText.error = "This filed can not be blank"
            false
        } else {
            true
        }
    }

    private fun checkValidKind(): Boolean {
        return if (binding.idKind.text.toString() != "Kind") {
            true
        } else {
            binding.idKind.error = "Select kind of new book"
            false
        }
    }


    private fun pushImageToStorage() {
        if (checkValidEditText(binding.idCount) && checkValidEditText(binding.idTitle) &&
            checkValidEditText(binding.idAuthor) && checkValidKind() && checkValidEditText(binding.idPrice) && checkValidEditText(
                binding.idDescription
            ) && imageUri != null

        ) {
            loadDialog = LoadDialog(this)
            loadDialog.startLoading()
            val imgId =
                System.currentTimeMillis().toString() + "." + getFileExtension(imageUri as Uri)
            val fileRef: StorageReference = reference.child(imgId)

            fileRef.putFile(imageUri as Uri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {
                    Toast.makeText(this, "Upload success image", Toast.LENGTH_SHORT).show()
                    addNewBook(it.toString(), imgId)
                    loadDialog.dismissDialog()
                }
            }.addOnFailureListener {
                loadDialog.dismissDialog()
                Toast.makeText(this, "Upload failed image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill all information", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(uri: Uri): String? {
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addNewBook(imgUrl: String, imgId: String) {
        newBook = mutableMapOf(
            "Image" to imgUrl,
            "Name" to binding.idTitle.text.toString(),
            "Author" to binding.idAuthor.text.toString(),
            "Price" to binding.idPrice.text.toString().toDouble().roundToInt(),
            "rate" to "0".toDouble().roundToInt(),
            "Kind" to binding.idKind.text.toString(),
            "Counts" to binding.idCount.text.toString().toDouble().roundToInt(),
            "Description" to binding.idDescription.text.toString(),
            "ImageID" to imgId
        )
        viewModel.addtoDb(newBook)
        binding.idCount.setText("", TextView.BufferType.EDITABLE)
        binding.idAuthor.setText("", TextView.BufferType.EDITABLE)
        binding.idTitle.setText("", TextView.BufferType.EDITABLE)
        binding.idDescription.setText("", TextView.BufferType.EDITABLE)
        binding.idPrice.setText("", TextView.BufferType.EDITABLE)
        binding.idKind.setText("", TextView.BufferType.EDITABLE)
        binding.idThumbnail.setImageDrawable(resources.getDrawable(R.drawable.add_new_book))
        binding.idTnBackground.setBackgroundDrawable(resources.getDrawable(R.drawable.add_new_book))
        Toast.makeText(this, "Add success", Toast.LENGTH_SHORT).show()
    }

}