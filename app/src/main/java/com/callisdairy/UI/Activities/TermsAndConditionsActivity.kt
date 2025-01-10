package com.callisdairy.UI.Activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.databinding.ActivityTermsAndConditionsBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.viewModel.StaticContentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermsAndConditionsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityTermsAndConditionsBinding

    var flag = ""

    private val viewModel : StaticContentViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent.getStringExtra("flag")?.let {
            flag = it
        }

        binding.BackClick.setOnClickListener {
            finishAfterTransition()
        }

        when (flag) {
            "about" -> {
                binding.Title.setText(R.string.about)
                viewModel.staticApi("aboutUs")
            }
            "terms" -> {
                binding.Title.setText(R.string.terms_conditions)
                viewModel.staticApi("termsConditions")

            }
            "privacy" -> {
                binding.Title.setText(R.string.privacy_policy)
                viewModel.staticApi("privacyPolicy")

            }
        }


        observeResponseRewards()

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun observeResponseRewards() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._staticContentData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {

//                                    binding.staticContent.settings.javaScriptEnabled = true
                                    binding.staticContent.webViewClient = MyWebViewClient()

                                    when (flag) {

                                        "about" -> {
                                            val html = response.data.result.description
                                            val formattedHtml = convertToHyperlinks(html)

                                            binding.staticContent.loadDataWithBaseURL(null, formattedHtml, "text/html", "UTF-8", null)

                                        }
                                        "terms" -> {
                                            val html = response.data.result.description
                                            val formattedHtml = convertToHyperlinks(html)

                                            binding.staticContent.loadDataWithBaseURL(null, formattedHtml, "text/html", "UTF-8", null)

                                        }
                                        else -> {
                                            val html = response.data.result.description
                                            val formattedHtml = convertToHyperlinks(html)

                                            binding.staticContent.loadDataWithBaseURL(null, formattedHtml, "text/html", "UTF-8", null)


                                        }
                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, this@TermsAndConditionsActivity)
                            }

                        }

                        is Resource.Loading -> {

                            Progresss.start(this@TermsAndConditionsActivity)


                        }

                        is Resource.Empty -> {
                            Progresss.stop()
                        }

                    }

                }

            }

        }


    }

    private inner class MyWebViewClient : WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // Handle URL loading
            if (url.startsWith("mailto:")) {
                // Open email client for mailto links
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                startActivity(intent)
                return true
            }
            // Load other URLs in the WebView
            view.loadUrl(url)
            return true
        }
    }



    private fun convertToHyperlinks(text: String): String {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val urlRegex = Regex("https?://\\S+")

        val emailMatcher = emailRegex.findAll(text)
        val urlMatcher = urlRegex.findAll(text)

        val replacedText = StringBuffer(text)

        // Replace email addresses with hyperlinks
        for (match in emailMatcher) {
            val email = match.value
            val hyperlink = "<a href=\"mailto:$email\">$email</a>"
            replacedText.replace(match.range.first, match.range.last + 1, hyperlink)
        }

        // Replace URLs with hyperlinks
        for (match in urlMatcher) {
            val url = match.value
            val hyperlink = "<a href=\"$url\">$url</a>"
            replacedText.replace(match.range.first, match.range.last + 1, hyperlink)
        }

        return replacedText.toString()
    }

}