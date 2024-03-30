package com.callisdairy.Utils


import android.content.Context
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.io.File


object CommonForImages {


    var base64Image: String? = null


    fun getMimeType(url: String?): String? {
        val file = url?.trim()?.let { File(it) }
        val extension = file?.extension
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }


    fun setValueDynamicallyInSpinner(context: Context, spinner: Spinner, setValue: String, degreeType: Int){
        val adapter = ArrayAdapter.createFromResource(context,degreeType, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val spinnerValues = context.resources.getStringArray(degreeType)
        var index = -1

        for (i in spinnerValues.indices) {
            if (spinnerValues[i] == setValue) {
                index = i
                break
            }
        }

        if (index != -1) {
            spinner.setSelection(index)
        }
    }










}