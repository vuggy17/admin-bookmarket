package com.example.admin_bookmarket.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Book(
    var id: String? = "1",
    var Image: String? = "",
    var Name: String? = "1",
    var Author: String? = "1",
    var Price: Int = 1,
    var rate: Int = 10,
    var Kind: String? ="1",
    var Counts:Int = 1,
    var imageId: String? ="1",
    var Description: String? = "1" ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(Image)
        parcel.writeString(Name)
        parcel.writeString(Author)
        parcel.writeInt(Price)
        parcel.writeInt(rate)
        parcel.writeString(Kind)
        parcel.writeInt(Counts)
        parcel.writeString(Description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
