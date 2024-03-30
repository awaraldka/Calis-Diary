package com.callisdairy.Utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.callisdairy.api.response.HomePageDocs
import com.callisdairy.api.response.suggestionListDocs
import javax.inject.Singleton

@Singleton
object Home {
    var pages = 0
    var page = 1
    var limit = 15
    var dataLoadFlag =  false
    var loaderFlag = true

    var data = ArrayList<HomePageDocs>()

    var flagHome= false

    var suggestedUserData = ArrayList<suggestionListDocs>()
    fun showKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}