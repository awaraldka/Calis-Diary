package com.callisdairy.Vendor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.callisdairy.R
import com.callisdairy.UI.Activities.Login
import com.callisdairy.Vendor.Activities.VendorLoginActivity
import com.callisdairy.databinding.ActivityChooseTypeBinding

class ChooseTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseTypeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade


        binding.UserLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

        }


        binding.RetailerLogin.setOnClickListener {
            val intent = Intent(this, VendorLoginActivity::class.java)
            startActivity(intent)

        }



    }
}