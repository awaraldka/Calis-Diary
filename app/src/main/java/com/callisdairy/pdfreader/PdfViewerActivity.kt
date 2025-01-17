package com.callisdairy.pdfreader

import android.Manifest.permission
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.webkit.CookieManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.callisdairy.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PdfViewerActivity : AppCompatActivity(), PdfRendererView.StatusCallBack {

    private var permissionGranted: Boolean? = false
    private var fileUrl: String? = null
    private lateinit var tvAppBarTitle:TextView
    lateinit var backPdf:ImageView
    lateinit var pdfView:PdfRendererView
    lateinit var progressBar:ProgressBar


    companion object {
        const val FILE_URL = "pdf_file_url"
        const val FILE_DIRECTORY = "pdf_file_directory"
        const val FILE_TITLE = "pdf_file_title"
        const val ENABLE_FILE_DOWNLOAD = "enable_download"
        const val FROM_ASSETS = "from_assests"
        var titlePdf = ""
        var engine = PdfEngine.INTERNAL
        var enableDownload = true
        var isPDFFromPath = false
        var isFromAssets = false

        var PERMISSION_CODE = 4040


        fun launchPdfFromUrl(
            context: Context?,
            pdfUrl: String?,
            pdfTitle: String?,
            directoryName: String?,
            titleName: String?,
            enableDownload: Boolean = true
        ): Intent {
            val intent = Intent(context, PdfViewerActivity::class.java)
            intent.putExtra(FILE_URL, pdfUrl)
            intent.putExtra(FILE_TITLE, pdfTitle)
            intent.putExtra(FILE_DIRECTORY, directoryName)
            intent.putExtra(ENABLE_FILE_DOWNLOAD, enableDownload)
            isPDFFromPath = false
            titlePdf = titleName!!
            return intent
        }

        fun launchPdfFromPath(
            context: Context?,
            path: String?,
            pdfTitle: String?,
            directoryName: String?,
            enableDownload: Boolean = true,
            fromAssets: Boolean = false
        ): Intent {
            val intent = Intent(context, PdfViewerActivity::class.java)
            intent.putExtra(FILE_URL, path)
            intent.putExtra(FILE_TITLE, pdfTitle)
            intent.putExtra(FILE_DIRECTORY, directoryName)
            intent.putExtra(ENABLE_FILE_DOWNLOAD, enableDownload)
            intent.putExtra(FROM_ASSETS, fromAssets)
            isPDFFromPath = true
            return intent
        }





    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        tvAppBarTitle = findViewById(R.id.tvAppBarTitle)
        backPdf = findViewById(R.id.backPdf)
        progressBar = findViewById(R.id.progressBar)
        pdfView = findViewById(R.id.pdfView)

        tvAppBarTitle.text = titlePdf.split(".pdf")[0]

        enableDownload = intent.extras!!.getBoolean(
            ENABLE_FILE_DOWNLOAD,
            true
        )
        backPdf.setOnClickListener {
            finishAfterTransition()
        }


        isFromAssets = intent.extras!!.getBoolean(
            FROM_ASSETS,
            false
        )

        engine = PdfEngine.INTERNAL

        init()
    }

    private fun init() {
        if (intent.extras!!.containsKey(FILE_URL)) {
            fileUrl = intent.extras!!.getString(FILE_URL)
            if (isPDFFromPath) {
                initPdfViewerWithPath(this.fileUrl)
            } else {
                if (checkInternetConnection(this)) {
                    loadFileFromNetwork(this.fileUrl)
                } else {
                    Toast.makeText(
                        this,
                        "No Internet Connection. Please Check your internet connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkInternetConnection(context: Context): Boolean {
        var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            result = 2
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            result = 1
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                            result = 3
                        }
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> {
                            result = 2
                        }
                        ConnectivityManager.TYPE_MOBILE -> {
                            result = 1
                        }
                        ConnectivityManager.TYPE_VPN -> {
                            result = 3
                        }
                    }
                }
            }
        }
        return result != 0
    }





    private fun loadFileFromNetwork(fileUrl: String?) {
        initPdfViewer(
            fileUrl,
            engine
        )
    }

    private fun initPdfViewer(fileUrl: String?, engine: PdfEngine) {
        if (TextUtils.isEmpty(fileUrl)) onPdfError()

        try {
            pdfView.initWithUrl(
                fileUrl!!,
                PdfQuality.NORMAL,
                engine
            )
        } catch (e: Exception) {
            onPdfError()
        }

        enableDownload()

    }

    private fun initPdfViewerWithPath(filePath: String?) {
        if (TextUtils.isEmpty(filePath)) onPdfError()

        //Initiating PDf Viewer with URL
        try {

            val file = if (isFromAssets)
                FileUtils.fileFromAsset(this, filePath!!)
            else File(filePath!!)

            pdfView.initWithFile(
                file,
                PdfQuality.NORMAL
            )

        } catch (e: Exception) {
            onPdfError()
        }

        enableDownload()
    }

    private fun enableDownload() {
        checkPermissionOnInit()

        val tvLoadingPercentage = findViewById<TextView>(R.id.tvLoadingPercentage)

        pdfView.statusListener = object : PdfRendererView.StatusCallBack {
            override fun onDownloadStart() {
                progressBar.visibility = View.VISIBLE
                tvLoadingPercentage.visibility = View.VISIBLE
                true.showProgressBar()
            }

            override fun onDownloadProgress(
                progress: Int,
                downloadedBytes: Long,
                totalBytes: Long?
            ) {
                val loadingPercentage = if (totalBytes != null && totalBytes > 0) {
                    val calculatedPercentage = (downloadedBytes * 100 / totalBytes).toInt()
                    minOf(calculatedPercentage, 99)
                } else {
                    0
                }

                // Show the loading percentage inside the TextView
                progressBar.progress = loadingPercentage
                tvLoadingPercentage.text = "$loadingPercentage%"
            }

            override fun onDownloadSuccess() {
                progressBar.progress = 100
                tvLoadingPercentage.text = "100%"
                progressBar.visibility = View.GONE
                tvLoadingPercentage.visibility = View.GONE
                false.showProgressBar()
            }

            override fun onError(error: Throwable) {
                progressBar.visibility = View.GONE
                tvLoadingPercentage.visibility = View.GONE
                onPdfError()
            }

            override fun onPageChanged(currentPage: Int, totalPage: Int) {
                // Page change. Not required.
            }
        }
    }



    private fun checkPermissionOnInit() {
        if (ContextCompat.checkSelfPermission(
                this,
                permission.WRITE_EXTERNAL_STORAGE
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        }
    }

    private fun onPdfError() {
        Toast.makeText(this, "Pdf has been corrupted", Toast.LENGTH_SHORT).show()
        true.showProgressBar()
        finish()
    }

    private fun Boolean.showProgressBar() {
        progressBar.visibility = if (this) View.VISIBLE else GONE
    }

    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(
                context,
                "File is Downloaded Successfully",
                Toast.LENGTH_SHORT
            ).show()
            context?.unregisterReceiver(this)
        }
    }

    private fun downloadPdf() {
        try {
            if (permissionGranted!!) {
                val directoryName = intent.getStringExtra(FILE_DIRECTORY)
                val fileName = intent.getStringExtra(FILE_TITLE)
                val fileUrl = intent.getStringExtra(FILE_URL)
                val filePath =
                    if (TextUtils.isEmpty(directoryName)) "/$fileName.pdf" else "/$directoryName/$fileName.pdf"

                try {
                    if (isPDFFromPath) {
                        FileUtils.downloadFile(
                            this,
                            fileUrl!!,
                            directoryName!!,
                            fileName
                        )
                    } else {
                        val downloadUrl = Uri.parse(fileUrl)
                        val downloadManger =
                            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                        val cookie = CookieManager.getInstance().getCookie(fileUrl);
                        val request = DownloadManager.Request(downloadUrl)
                        request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI or
                                    DownloadManager.Request.NETWORK_MOBILE
                        )
                        request.setAllowedOverRoaming(true)
                        request.setTitle(fileName)
                        request.setDescription("Downloading $fileName")
                        request.setVisibleInDownloadsUi(true)
                        request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            filePath
                        )
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        if (!TextUtils.isEmpty(cookie))
                            request.addRequestHeader("Cookie", cookie)
                        registerReceiver(
                            onComplete,
                            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                        )
                        downloadManger!!.enqueue(request)
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Unable to download file",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                checkPermissionOnInit()
            }

        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }

    private fun checkPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(permission.WRITE_EXTERNAL_STORAGE),
                requestCode
            )
        } else {
            permissionGranted = true
            downloadPdf()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
            downloadPdf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pdfView.closePdfRender()
    }






}