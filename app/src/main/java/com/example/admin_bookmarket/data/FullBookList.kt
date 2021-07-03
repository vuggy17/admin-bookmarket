package com.example.admin_bookmarket.data

import android.util.Log
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.math.roundToInt

public class FullBookList private constructor(var lstFullBook: MutableList<Book> = mutableListOf()) {

    init {
        getDataBySnapshot()
    }

    private fun getDataBySnapshot() {
        var ref = FirebaseFirestore.getInstance().collection("books")
        ref.addSnapshotListener (object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }
                val bookList: MutableList<Book> = ArrayList()
                for (doc in value!!) {
                    if (doc.data["Saler"].toString() == AppUtil.currentAccount.email ) {
                        var bookItem = Book(
                            doc.id,
                            doc.data["Image"].toString(),
                            doc.data["Name"].toString(),
                            doc.data["Author"].toString(),
                            doc.data["Price"].toString().toDouble().roundToInt(),
                            doc.data["rate"].toString().toDouble().roundToInt(),
                            doc.data["Kind"].toString(),
                            doc.data["Counts"].toString().toDouble().roundToInt(),
                            doc.data["ImageID"].toString(),
                            doc.data["Description"].toString(),
                            doc.data["Saler"].toString(),
                            doc.data["SalerName"].toString()
                        )
                        bookList.add(bookItem)
                    }
                }
                lstFullBook = bookList
            }
        })
    }
    private object Holder {
        val INSTANCE = FullBookList()
    }

    companion object {

        fun getInstance(): FullBookList {
            return Holder.INSTANCE
        }

//        private fun getDataFromDb(): MutableList<Book> {
//            val lstFullBook: MutableList<Book> = mutableListOf()
//            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//            val ref = db.collection("books")
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                        val doc = document.data;
//                        val authors: String = document.get("Author").toString();
//                        val count: Long = document.get("Counts").toString().toLong();
//                        val description: String = document.get("Description").toString();
//                        val image: Uri = Uri.parse(document.get("Image").toString());
//                        val kind: String = document.get("Kind").toString();
//                        val name: String = document.get("Name").toString();
//                        val rate: Double = document.get("rate").toString().toDouble();
//                        val price: Long = document.get("Price").toString().toLong();
//                        val book: Book = Book(
//                            document.id,
//                            image,
//                            name,
//                            authors,
//                            price,
//                            rate,
//                            kind,
//                            count,
//                            description
//                        )
//                        lstFullBook.add(book)
//                    }
//                }
//
//            return lstFullBook
//        }

    }
}