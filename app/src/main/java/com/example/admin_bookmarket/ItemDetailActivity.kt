package com.example.admin_bookmarket


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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.admin_bookmarket.ViewModel.ItemDetailViewModel
import com.example.admin_bookmarket.data.model.Book
import com.example.admin_bookmarket.databinding.ActivityItemDetailBinding
import com.example.admin_bookmarket.ui.login.LoadDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


@AndroidEntryPoint
class ItemDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityItemDetailBinding
    private val viewModel: ItemDetailViewModel by viewModels()
    private var displayItem: Book = Book()
    private var imageUri: Uri? = null
    private var oldImageUrl: String? = ""
    private var oldImageId: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusValue = resources.getStringArray(R.array.kind)
        val arrayAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.status_dropdown_item,
            statusValue
        )
        binding.idKind.setAdapter(arrayAdapter)

        viewModel.book.observe(this, changeObserver)

        setupOnClickListener()


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
            oldImageId = ""
            oldImageUrl = ""
        }
    }

    private fun deleteCurrentBook() {
        viewModel.deleteBook(displayItem)
        startActivity(Intent(baseContext, MainActivity::class.java))
    }

    private val changeObserver = Observer<Book> { value ->
        value?.let {
            displayItem = value
            binding.idAuthor.setText(value.Author, TextView.BufferType.EDITABLE)
            binding.idPrice.setText(value.Price.toString(), TextView.BufferType.EDITABLE)
            binding.idRate.setText(value.rate.toString(), TextView.BufferType.EDITABLE)
            binding.idTitle.setText(value.Name, TextView.BufferType.EDITABLE)
            binding.idKind.setText(value.Kind, false)
            binding.idCount.setText(value.Counts.toString(), TextView.BufferType.EDITABLE)
            binding.idDescription.setText(value.Description, TextView.BufferType.EDITABLE)
            oldImageUrl = value.Image
            oldImageId = value.imageId
            it.Image?.let { uri -> loadImageFromUri(Uri.parse(uri)) }
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

    private fun setupOnClickListener() {
        binding.idBack.setOnClickListener { onBackPressed() }
        binding.idUpdate.setOnClickListener { updateToDb() }
        binding.idThumbnail.setOnClickListener { openGallery() }
        binding.dismissImage.setOnClickListener { dismissChangeButton() }
    }

    private fun dismissChangeButton() {
        oldImageUrl = displayItem.Image
        oldImageId = displayItem.imageId
        displayItem.Image?.let { uri -> loadImageFromUri(Uri.parse(uri)) }

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

    private val reference: StorageReference = FirebaseStorage.getInstance().reference
    private lateinit var loadDialog: LoadDialog
    private fun updateToDb() {
        if (checkValidEditText(binding.idCount) &&
            checkValidEditText(binding.idTitle) &&
            checkValidEditText(binding.idAuthor) &&
            checkValidKind() &&
            checkValidEditText(binding.idPrice) &&
            checkValidEditText(binding.idDescription)
        ) {
            var newBook = Book(
                displayItem.id,
                oldImageUrl,
                binding.idTitle.text.toString(),
                binding.idAuthor.text.toString(),
                binding.idPrice.text.toString().toDouble().roundToInt(),
                binding.idRate.text.toString().toDouble().roundToInt(),
                binding.idKind.text.toString(),
                binding.idCount.text.toString().toDouble().roundToInt(),
                oldImageId,
                binding.idDescription.text.toString()
            )
            if (newBook != displayItem) {
                if (imageUri != null) {
                    loadDialog = LoadDialog(this)
                    loadDialog.startLoading()
                    val imgId =
                        System.currentTimeMillis()
                            .toString() + "." + getFileExtension(imageUri as Uri)
                    val fileRef: StorageReference = reference.child(imgId)
                    fileRef.putFile(imageUri as Uri).addOnSuccessListener {
                        fileRef.downloadUrl.addOnSuccessListener {
                            Toast.makeText(this, "Upload success image", Toast.LENGTH_SHORT).show()
                            val desertRef = reference.child(displayItem.imageId!!)
                            desertRef.delete()
                            newBook.imageId = imgId
                            newBook.Image = it.toString()
                            viewModel.updateToDb(displayItem.id!!, newBook)
                            loadDialog.dismissDialog()
                        }
                    }.addOnFailureListener {
                        loadDialog.dismissDialog()
                        Toast.makeText(this, "Upload failed image", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    viewModel.updateToDb(displayItem.id!!, newBook)
                }
            }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        var cr: ContentResolver = contentResolver
        var mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }
}