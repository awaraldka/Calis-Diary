package com.callisdairy.Utils

import org.mozilla.javascript.Context
import org.mozilla.javascript.Function
import org.mozilla.javascript.Scriptable
import java.io.InputStream
import java.util.Scanner

class AES(var context: android.content.Context) {
    var inputStream: InputStream? = null
    lateinit var rhino: Context
    var scope: Scriptable? = null
    fun set_values() {
        inputStream = context.resources.openRawResource(
            context.resources.getIdentifier(
                "aes",
                "raw", context.packageName
            )
        )
        val data = convertStreamToString(inputStream)
        rhino = Context.enter()!!

        // Turn off optimization to make Rhino Android compatible
        rhino.optimizationLevel = -1
        scope = rhino.initStandardObjects()
        rhino.evaluateString(scope, data, "JavaScript", 0, null)
    }

    fun encrypt(data: String, key: String): String? {
        set_values()
        val obj = scope!!["encryption", scope]
        if (obj is Function) {
            val params = arrayOf<Any>(data, key)

            // Call the function with params
            val jsResult = obj.call(rhino, scope, scope, params)
            // Parse the jsResult object to a String
            return Context.toString(jsResult)
        }
        return null
    }

    fun decrypt(data: String, key: String): String? {
        set_values()
        val obj = scope!!["decryption", scope]
        if (obj is Function) {
            val params = arrayOf<Any>(data, key)
            // Call the function with params
            val jsResult = obj.call(rhino, scope, scope, params)
            // Parse the jsResult object to a String
            return Context.toString(jsResult)
        }
        return null
    }

    companion object {
        fun convertStreamToString(`is`: InputStream?): String {
            val s = Scanner(`is`).useDelimiter("\\A")
            return if (s.hasNext()) s.next() else ""
        }
    }
}