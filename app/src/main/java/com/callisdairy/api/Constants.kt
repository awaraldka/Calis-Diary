package com.callisdairy.api

import com.callisdairy.api.response.KeysData

object Constants {


//      Raghav

//    const val BASE_URL = "http://172.16.2.18:1957/api/v1/"
//    val SOCKET_URL = "http://172.16.2.18:1957"


 //    Production Url

    const val BASE_URL = "https://node.calisdiary.com/api/v1/"
    val SOCKET_URL = "https://socket.calisdiary.com"


//    Staging Url

//        const val BASE_URL = "https://node-calisdiary2.mobiloitte.io/api/v1/"
//        val SOCKET_URL = "https://node-calisdiary2.mobiloitte.io"v

    const val NO_INTERNET = "No Internet Connection"


    // Rishab Sir

//    val SOCKET_URL = "http://172.16.1.247:1957"
//    const val BASE_URL = "http://172.16.1.247:1957/api/v1/"

    private var keysData: KeysData? = null

    fun initializeKeys(keysData: KeysData) {
        this.keysData = keysData
    }

    fun getKeysData(): KeysData? {
        return keysData
    }


}