package com.callisdairy.UI.Activities

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.RetrievePDFFromURL
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class PdfViewActivity : AppCompatActivity() {

    private lateinit var pdfView: PDFView
    private lateinit var BackClick: ImageView
    var doc =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)
        intent.getStringExtra("pdf")?.let { doc = it }
        window.attributes.windowAnimations = R.style.Fade
        pdfView = findViewById(R.id.pdfView)
        BackClick = findViewById(R.id.BackClick)

        BackClick.setOnClickListener {
            finishAfterTransition()
        }

        RetrievePDFFromURL(this,pdfView).execute(doc)

    }

}
