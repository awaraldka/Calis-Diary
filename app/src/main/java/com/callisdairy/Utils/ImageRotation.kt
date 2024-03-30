package com.callisdairy.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.exifinterface.media.ExifInterface
import com.callisdairy.R
import com.callisdairy.Validations.FormValidations.startActivityForResult
import com.theartofdev.edmodo.cropper.CropImage
import java.io.*
import java.util.Base64
import java.util.UUID
import kotlin.random.Random


object ImageRotation {

    fun getRealPathFromURI2(context: Context, contentUri: Uri): String? {
        val out: OutputStream
        val file: File = File(getFilename(context))
        try {
            if (file.createNewFile()) {
                val iStream: InputStream? =
                    context.contentResolver.openInputStream(
                        contentUri
                    )
                val inputData: ByteArray =
                    iStream?.let { getBytes(it) }!!
                out = FileOutputStream(file)
                out.write(inputData)
                out.close()
                return file.absolutePath
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class)
    private fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    private fun getFilename(context: Context): String? {
        val mediaStorageDir = File(context.getExternalFilesDir(""), "Callis Images")
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs()
        }
        val mImageName = "IMG_" + System.currentTimeMillis().toString() + ".jpg"
        return mediaStorageDir.absolutePath + "/" + mImageName
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getBitmap(path: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val f = File(path)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return bitmap
    }



    fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String?): Bitmap? {
        val ei = image_absolute_path?.let { ExifInterface(it) }
        val orientation: Int =
            ei!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap, true, false)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, false, true)
            else -> bitmap
        }
    }

    fun rotate(bitmap: Bitmap, degrees: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap? {
        val matrix = Matrix()
        matrix.preScale(
            if (horizontal) (-1).toFloat() else 1.toFloat(),
            if (vertical) (-1).toFloat() else 1.toFloat()
        )
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    fun bitmapToString(`in`: Bitmap): String {
        var options = 50
        var base64_value = ""
        var base64 = ""
        val bytes = ByteArrayOutputStream()
        `in`.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
        while (bytes.toByteArray().size / 1024 > 400) {
            bytes.reset() //Reset baos is empty baos
            `in`.compress(Bitmap.CompressFormat.JPEG, options, bytes)
            options -= 10
        }
        base64 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(bytes.toByteArray())
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        base64_value = base64_value.replace("\n", "") + base64
        return base64_value
    }

    fun writeFileContent(uri: Uri, context: Context): String? {
        val selectedFileInputStream = context.contentResolver.openInputStream(uri) as? InputStream
        if (selectedFileInputStream != null) {
            val mediaDir = File(context.getExternalFilesDir(null), context.resources.getString(R.string.app_name)).apply { mkdirs() }

            val certCacheDir = File(mediaDir, "FILE_BROWSER_CACHE_DIR")
            var isCertCacheDirExists = certCacheDir.exists()
            if (!isCertCacheDirExists) {
                isCertCacheDirExists = certCacheDir.mkdirs()
            }
            if (isCertCacheDirExists) {
                val fileName: String? = getFileDisplayName(uri, context)
                fileName?.replace("[^a-zA-Z0-9]", " ")
                val filePath = certCacheDir.absolutePath.toString() + "/" + fileName
                val selectedFileOutPutStream: OutputStream = FileOutputStream(filePath)
                val buffer = ByteArray(1024)
                var length: Int
                while (selectedFileInputStream.read(buffer).also { length = it } > 0) {
                    selectedFileOutPutStream.write(buffer, 0, length)
                }
                selectedFileOutPutStream.flush()
                selectedFileOutPutStream.close()
                return filePath
            }
            selectedFileInputStream.close()
        }
        return null
    }

    @SuppressLint("Range")
    @Nullable
    fun getFileDisplayName(uri: Uri, context: Context): String? {
        var displayName: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return displayName
    }


    fun generateRandomImageName(): String {
        val uniqueId = UUID.randomUUID().toString() // Generate a unique identifier
        val timestamp = System.currentTimeMillis() // Get the current timestamp
        val randomInt = Random.nextInt(1000) // Generate a random number between 0 and 999

        return "image_$uniqueId$timestamp$randomInt.jpg" // Create the random image name
    }




}