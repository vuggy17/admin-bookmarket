package com.example.admin_bookmarket.ui.login

import android.app.Activity
import android.app.AlertDialog
import com.example.admin_bookmarket.R

class LoadDialog(myActivity: Activity) {
    private var activity: Activity = myActivity
    private lateinit var dialog: AlertDialog

    fun startLoading(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.activity_load_screen,null))

        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }
    fun dismissDialog(){
        dialog.dismiss()
    }

}