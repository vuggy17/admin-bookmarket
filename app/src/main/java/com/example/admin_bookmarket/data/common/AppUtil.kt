package com.example.admin_bookmarket.data.common

import com.example.admin_bookmarket.data.model.AppAccount
import com.example.admin_bookmarket.data.model.Book
import com.example.admin_bookmarket.data.model.Order
import com.example.admin_bookmarket.data.model.User
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlin.math.roundToInt

object AppUtil {
    var currentUser: User = User()
    var currentAccount: AppAccount = AppAccount("", "", currentUser)
    var currentOrder: Order = Order()

    fun toBook(doc: QueryDocumentSnapshot): Book {
        var bookItem = Book()
        bookItem.Author = doc["Author"].toString()
        bookItem.Counts = doc["Counts"].toString().toDouble().roundToInt()
        bookItem.Description = doc["Description"].toString()
        bookItem.Image = doc["Image"].toString()
        bookItem.Kind = doc["Kind"].toString()
        bookItem.Name = doc["Name"].toString()
        bookItem.Price = doc["Price"].toString().toDouble().roundToInt()
        bookItem.rate = doc["rate"].toString().toDouble().roundToInt()
        return bookItem
    }

    fun checkName(str: String): Boolean {
        val regex =
            "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_][^\\d?!@#\\\$%\\^\\&*\\)\\(:';,\"+=._-`~{}|/\\\\]{1,}\$".toRegex()
        return str.matches(regex)
    }

    fun checkAddress(str: String): Boolean {
        val regex =
            "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_][^?!@#\\\$%\\^\\&*\\)(:'\"+=_-`~{}|]{3,}\$".toRegex()
        return str.matches(regex)
    }

    fun checkPhoneNumber(str:String):Boolean{
        val regex = "(84|0[3|5|7|8|9])+([0-9]{8})\\b".toRegex()
        return str.matches(regex)
    }


}