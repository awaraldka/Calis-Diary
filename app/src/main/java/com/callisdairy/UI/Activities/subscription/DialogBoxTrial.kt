package com.callisdairy.UI.Activities.subscription

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener

class DialogBoxTrial() : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.free_trial_popup, container, false)
        val yesBtn = view.findViewById<Button>(R.id.Continue_button_popup)
        val description = view.findViewById<TextView>(R.id.description)

        val htmlContent = convertToHyperlinks()
        displayHtmlText(description, htmlContent)


        yesBtn.setSafeOnClickListener {
            dismiss()


        }
        return view
    }

    override fun onStart() {
        super.onStart()
//        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog?.window?.setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(false)
    }

    private fun convertToHyperlinks(): String {
        val text = "<p><strong>Don't miss out on this limited-time offer. Experience all the fantastic features, incredible content, and top-notch services at no cost. It's our way of saying thank you for choosing us!</strong></p>\n" +
                "<p>Ready to get started? Simply update your app or install it today and access premium features instantly.</p>\n" +
                "<p>Discover the possibilities, explore without limits, and make the most of your 90 days of free premium service.</p>\n" +
                "<p>Have questions or need assistance? Our support team is here to help. Contact us at example@example.com.</p>\n" +
                "<p>Thank you for being a valued part of our community. Enjoy the journey!</p>\n"

        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val urlRegex = Regex("https?://\\S+")

        // Replace email addresses with hyperlinks
        var replacedText = emailRegex.replace(text) { matchResult ->
            val email = matchResult.value
            "<a href=\"mailto:$email\">$email</a>"
        }

        // Replace URLs with hyperlinks
        replacedText = urlRegex.replace(replacedText) { matchResult ->
            val url = matchResult.value
            "<a href=\"$url\">$url</a>"
        }

        return replacedText
    }

    // Function to parse and display the HTML content in a TextView
    fun displayHtmlText(textView: TextView, htmlContent: String) {
        val spanned: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlContent)
        }
        textView.text = spanned
        textView.movementMethod = android.text.method.LinkMovementMethod.getInstance() // To enable clicking on the links
    }



}