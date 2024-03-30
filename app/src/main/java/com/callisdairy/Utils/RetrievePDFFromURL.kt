package com.callisdairy.Utils

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

open class RetrievePDFFromURL(context: Context, pdfView: PDFView) : AsyncTask<String, Void, InputStream>() {

    private val contextRef: WeakReference<Context> = WeakReference(context)
    private val pdfViewRef: WeakReference<PDFView> = WeakReference(pdfView)

    override fun doInBackground(vararg params: String?): InputStream? {
        var inputStream: InputStream? = null

        try {
            val url = URL(params[0])

            val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
            if (urlConnection.responseCode == 200) {
                inputStream = BufferedInputStream(urlConnection.inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return inputStream
    }

    override fun onPostExecute(result: InputStream?) {
        val context = contextRef.get()
        val pdfView = pdfViewRef.get()

        if (context != null && pdfView != null) {
            if (result != null) {
                pdfView.fromStream(result).load()
            } else {
                Toast.makeText(context, "Failed to retrieve PDF from URL", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
