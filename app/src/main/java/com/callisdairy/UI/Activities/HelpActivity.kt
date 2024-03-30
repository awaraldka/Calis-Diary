package com.callisdairy.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.callisdairy.R
import com.callisdairy.databinding.ActivityHelpBinding
import com.callisdairy.databinding.ActivitySettingsBinding
import com.callisdairy.extension.setSafeOnClickListener

class HelpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHelpBinding
    var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        intent?.getStringExtra("from")?.let { from = it }


        binding.BackClick.setSafeOnClickListener {
            finishAfterTransition()
        }


        if (from == "vendor"){
            binding.ExploreButton.isVisible = false
        }



        binding.aboutButton.setSafeOnClickListener {
            val intent = Intent(this, TermsAndConditionsActivity::class.java)
            intent.putExtra("flag", "about")
            startActivity(intent)

        }

        binding.FAQButton.setSafeOnClickListener {
            val intent = Intent(this, FAQActivity::class.java)
            intent.putExtra("flag", "faq")
            startActivity(intent)

        }

        binding.termsButton.setSafeOnClickListener {
            var intent = Intent(this, TermsAndConditionsActivity::class.java)
            intent.putExtra("flag", "terms")
            startActivity(intent)

        }

        binding.privacyButton.setSafeOnClickListener {
            val intent = Intent(this, TermsAndConditionsActivity::class.java)
            intent.putExtra("flag", "privacy")
            startActivity(intent)

        }

        binding.ExploreButton.setSafeOnClickListener {
            startActivity(Intent(this, ExploreActivity::class.java))
        }

        binding.ContactButton.setSafeOnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        }









    }
}