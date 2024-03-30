package com.callisdairy.UI.Activities.subscription

import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.callisdairy.Interface.RenewSubscription
import com.callisdairy.R
import com.callisdairy.databinding.ActivitySubscriptionManageBinding
import com.callisdairy.extension.setSafeOnClickListener

class SubscriptionManageActivity : AppCompatActivity(), RenewSubscription {

    private lateinit var binding: ActivitySubscriptionManageBinding


    var isExpired = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade


        intent.getBooleanExtra("expired",false)?.let {
            isExpired = it

        }

        if (isExpired){
            supportFragmentManager.let { DialogBoxExpiredRenew(this@SubscriptionManageActivity, "","").show(it, "MyCustomFragment") }
        }




        // Initialize Stripe with your publishable key


        binding.Payment.setSafeOnClickListener {
            val intent = Intent(this,CardDetailsActivity::class.java)
            startActivity(intent)
            finishAfterTransition()
        }



        binding.setTextHtml.settings.javaScriptEnabled = true

        val htmlContent = """
            <ul>
              <li><strong>Premium Access:</strong> Subscribing to our premium plan unlocks exclusive features and content that enhance your experience.</li> <br>
              <li><strong>Post Content:</strong> Access a vast library of content without any restrictions. Explore a wide range of resources at your convenience.</li>
            </ul>
        """.trimIndent()

        // Load HTML content into the WebView
        binding.setTextHtml.loadData(htmlContent, "text/html", "UTF-8")

        // Optionally, you can set a WebViewClient to handle navigation
        binding.setTextHtml.webViewClient = WebViewClient()





    }

    override fun renewNow() {

    }


}
