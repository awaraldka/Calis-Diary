package com.callisdairy.UI.Activities

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import java.util.regex.Matcher
import java.util.regex.Pattern


class PatternEditableBuilder {
    // Records the pattern spans to apply to a TextView
    var patterns: ArrayList<SpannablePatternItem>

    /* This stores a particular pattern item
       complete with pattern, span styles, and click listener */
    inner class SpannablePatternItem(
        pattern: Pattern,
        styles: SpannableStyleListener?,
        listener: SpannableClickedListener?
    ) {
        var styles: SpannableStyleListener?
        var pattern: Pattern
        var listener: SpannableClickedListener?

        init {
            this.pattern = pattern
            this.styles = styles
            this.listener = listener
        }
    }

    /* This stores the style listener for a pattern item
       Used to style a particular category of spans */
    abstract class SpannableStyleListener {
        var spanTextColor = 0

        constructor() {}
        constructor(spanTextColor: Int) {
            this.spanTextColor = spanTextColor
        }

        abstract fun onSpanStyled(ds: TextPaint?)
    }

    /* This stores the click listener for a pattern item
       Used to handle clicks to a particular category of spans */
    interface SpannableClickedListener {
        fun onSpanClicked(text: String?)
    }

    /* This is the custom clickable span class used
       to handle user clicks to our pattern spans
       applying the styles and invoking click listener.
     */
    inner class StyledClickableSpan(var item: SpannablePatternItem) :
        ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            if (item.styles != null) {
                item.styles!!.onSpanStyled(ds)
            }
            super.updateDrawState(ds)
        }

        override fun onClick(widget: View) {
            if (item.listener != null) {
                val tv = widget as TextView
                val span = tv.text as Spanned
                val start = span.getSpanStart(this)
                val end = span.getSpanEnd(this)
                val text = span.subSequence(start, end)
                item.listener!!.onSpanClicked(text.toString())
            }
            widget.invalidate()
        }
    }

    /* ----- Constructors ------- */
    init {
        patterns = ArrayList()
    }

    /* These are the `addPattern` overloaded signatures */ // Each allows us to add a span pattern with different arguments

    fun addPattern(
        pattern: Pattern,
        spanStyles: SpannableStyleListener?,
        listener: SpannableClickedListener?
    ): PatternEditableBuilder {
        patterns.add(SpannablePatternItem(pattern, spanStyles, listener))
        return this
    }

    fun addPattern(pattern: Pattern, spanStyles: SpannableStyleListener?): PatternEditableBuilder {
        addPattern(pattern, spanStyles, null)
        return this
    }

    fun addPattern(pattern: Pattern): PatternEditableBuilder {
        addPattern(pattern, null, null)
        return this
    }

    fun addPattern(pattern: Pattern, textColor: Int): PatternEditableBuilder {
        addPattern(pattern, textColor, null)
        return this
    }

    fun addPattern(
        pattern: Pattern,
        textColor: Int,
        listener: SpannableClickedListener?
    ): PatternEditableBuilder {
        val styles: SpannableStyleListener = object : SpannableStyleListener(textColor) {
            override fun onSpanStyled(ds: TextPaint?) {
//                ds?.linkColor = spanTextColor
            }
        }
        addPattern(pattern, styles, listener)
        return this
    }

    fun addPattern(pattern: Pattern, listener: SpannableClickedListener?): PatternEditableBuilder {
        addPattern(pattern, null, listener)
        return this
    }

    /* BUILDER METHODS */ // This builds the pattern span and applies to a TextView
    fun into(textView: TextView) {
        val result = build(textView.text)
        textView.text = result
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    // This builds the pattern span into a `SpannableStringBuilder`
    // Requires a CharSequence to be passed in to be applied to
    fun build(editable: CharSequence?): SpannableStringBuilder {
        val ssb = SpannableStringBuilder(editable)
        for (item in patterns) {
            val matcher: Matcher = item.pattern.matcher(ssb)
            while (matcher.find()) {
                val start: Int = matcher.start()
                val end: Int = matcher.end()
                val url = StyledClickableSpan(item)
                ssb.setSpan(url, start, end, 0)
            }
        }
        return ssb
    }
}