package com.callisdairy.UI.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.callisdairy.R

class SelectImageActivity : AppCompatActivity() {

    val PICK_IMAGE_MULTIPLE = 1
    private var mArrayUri = ArrayList<Uri>()
    lateinit var imageView: ImageSwitcher
    var position = 0
    lateinit var select: Button
    lateinit var previous: Button
    lateinit var next: Button
    lateinit var total: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image)
        select = findViewById(R.id.select)
        total = findViewById(R.id.text)
        imageView = findViewById(R.id.image)
        previous = findViewById(R.id.previous)
        next = findViewById(R.id.next)


        imageView.setFactory { ImageView(applicationContext) }




        next.setOnClickListener {
            if (position < mArrayUri.size - 1) {
                position++
                imageView.setImageURI(mArrayUri[position])
            } else {
                Toast.makeText(this, "Last Image Already Shown", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        previous.setOnClickListener {
            if (position > 0) {
                position--
                imageView.setImageURI(mArrayUri[position])
            }
        }



        select.setOnClickListener {
            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/* video/*"
            pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

            startActivityForResult(pickIntent, PICK_IMAGE_MULTIPLE)
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            if (data.clipData != null) {
                val mClipData = data.clipData
                val cout = data.clipData!!.itemCount
                for (i in 0 until cout) {
                    val imageurl = data.clipData!!.getItemAt(i).uri
                    mArrayUri.add(imageurl)
                }
                imageView.setImageURI(mArrayUri[0])
                position = 0
            } else {
                val imageurl = data.data
                mArrayUri.add(imageurl!!)
                imageView.setImageURI(mArrayUri[0])
                position = 0
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }

}

