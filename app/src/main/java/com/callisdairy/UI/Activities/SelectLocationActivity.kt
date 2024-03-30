package com.callisdairy.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.callisdairy.R
import com.callisdairy.databinding.ActivitySelectLocationBinding

class SelectLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectLocationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }
    }
}