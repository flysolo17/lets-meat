package com.ciejaycoding.letsmeat.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.ciejaycoding.letsmeat.R

import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProgressDialog(private val context: Context) {
     var alertDialog: androidx.appcompat.app.AlertDialog ? = null
    fun showLoadingDialog(title : String){
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
        val view : View = LayoutInflater.from(context).inflate(R.layout.loading_dialog,null)
        view.findViewById<TextView>(R.id.textTitle).text = title
        materialAlertDialogBuilder.setView(view)
        materialAlertDialogBuilder.setCancelable(false)
        alertDialog = materialAlertDialogBuilder.create()
        alertDialog?.show()
    }
    fun stopLoading(){
        alertDialog?.dismiss()
    }
}