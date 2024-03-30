package com.callisdairy.Socket

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.callisdairy.api.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import kotlin.collections.ArrayList

class SocketManager private constructor(context: Context) {

    val isConnected: Boolean get() = socket.connected()
    private lateinit var socketId: String
    lateinit var socket: Socket
    lateinit var socketListener: SocketListener

    /*Method to connect Socket */
    fun connect() {
        if (!socket.connected())
            socket.connect()
    }


    fun sendMsg(key: String, vararg args: Any) {
        if (socket.connected()) {
            socket.emit(key, *args)
            Log.e("browse_page_err", "oooSocket Connect--$key $args")

        }
    }

    fun onlineUser(s: String) {
        Log.e("onlineUser", s.toString())
        val jsonObject = JSONObject().put("userId", s)
        socket.emit("onlineUser", jsonObject)

    }

    fun offlineUser(s: String) {
        val jsonObject = JSONObject().put("userId", s)
        socket.emit("offlineUser", jsonObject)

    }

    fun Update(text: String, USERID: String, reciver_id: String) {
        val data = JSONObject()
        data.put("senderId", USERID);
        data.put("receiverId", reciver_id);
        data.put("message", text);
        socket!!.emit("oneToOneChat", data);
        // socket?.emit("new message", "message");
    }

    fun oneToOneChat(sender: String, reciver_id: String,message:String,mediaType:String,attachment:String) {
        val data = JSONObject()
        data.put("senderId", sender)
        data.put("receiverId", reciver_id)
        data.put("message", message)
        data.put("mediaType", mediaType)
        data.put("attachment", attachment)
        socket.emit("oneToOneChat", data)
    }



    fun chatHistory(USERID: String,receiverId:String) {
        val data = JSONObject()
        data.put("senderId", USERID)
        data.put("receiverId", receiverId)
        socket.emit("chatHistory", data);
    }


    fun chatDataList(USERID: String) {
        val data = JSONObject()
        data.put("userId", USERID);
        socket.emit("chatList", data);
    }


    fun checkOnlineUser(USERID: String) {
        val data = JSONObject()
        data.put("userId", USERID);
        socket.emit("checkOnlineUser", data);
    }


    fun typeUser(senderId: String,receiverId: String,status:Boolean) {
        val data = JSONObject()
        data.put("senderId", senderId)
        data.put("receiverId", receiverId)
        data.put("status", status)
        socket.emit("typing", data);
    }


    fun removeListener(key: String) {
        socket.off(key)
    }

    /* Add Listener to Socket*/
    fun addListener(key: String, param: SocketMessageListener) {
        Log.e("browse_page_err", "getcheckSocket Connect--$key")

        socket.on(key) { args ->
            Log.e("browse_page_err", "wow Socket Connect--$key $args")
            Handler(Looper.getMainLooper()).post {
                if (args != null && args.isNotEmpty()) {
                    //socketMessageListener.onMessage(*args)
                }
            }
        }
    }


    fun initialize(socketList: SocketListener) {
        socketListener = socketList
    }

    /* Disconnect Socket*/
    fun disConnect() {
        if (socket.connected())
            socket.disconnect()
    }


    interface SocketListener {
        fun onConnected()
        fun onDisConnected()
        fun chatHistroy(listdat: ArrayList<ChatHistoryResult>)
        fun chatListData(listdat: ArrayList<chatDataResult>)
        fun viewchat(listdat: java.util.ArrayList<MessagesChat>)
        fun oneToOneChat(listdat: ChatHistoryResult)
        fun onlineUser(listdat: ArrayList<OnlineUserResult>)
        fun offlineUser(listdat: CheckOnlineUserResult)
        fun checkOnlineUser(listdat: CheckOnlineUserResult)
        fun typing(listdat: JsonObject)
        fun typingUser(listdat: UserTypingResult)

    }

    /* Interface to Handle Message event of Socket*/
    interface SocketMessageListener {
        fun onMessage(vararg args: Any)
    }

    companion object {
        private val TAG = SocketManager::class.java.simpleName
        //lateinit var socket: Socket

        @get:Synchronized
        var socketManager: SocketManager? = null

        /**
         * Method to get the instance of socket class
         *
         * @param context
         * @return
         */

        fun getInstance(context: Context): SocketManager {
            if (socketManager == null) {
                socketManager = SocketManager(context)
            }
            return socketManager as SocketManager
        }
    }

    init {
        try {

            val options = IO.Options()
            options.reconnection = true
            options.reconnectionDelay = 100
            options.reconnectionAttempts = 40
            options.secure = true
            options.timeout = 900000
            socket = IO.socket(Constants.SOCKET_URL)
            socket.on(Socket.EVENT_CONNECT) {
                Handler(Looper.getMainLooper()).post {
                    if (socket.connected()) {
                        socketId = socket.id()
                        println("Socketid=$socketId")
                        socketListener.onConnected()

                    }
                }
            }
                .on(Socket.EVENT_DISCONNECT) { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty()) {
                            Log.d(TAG, "Socket NotConnect :- ")
                            Log.e("browse_page_err", "" + "Socket NotConnect")
                        }
                    }
                }
                .on("onlineUser") { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty() && args[0] != null) {
                            val gson = Gson()
                            val jsonString = args[0].toString()

                            if (jsonString != null) {
                                val fcmResponse: OnlineUserResponse = gson.fromJson(jsonString, OnlineUserResponse::class.java)
                                print("++++++++++++>>> ${args.getOrNull(0)}")
                                socketListener.onlineUser(fcmResponse.result)
                            }
                        }
                    }
                }

                .on("offlineUser") { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty()) {
                            val gson = Gson()
                            val fcmResponse: CheckOnlineUserResponse = gson.fromJson(args.getOrNull(0).toString(), CheckOnlineUserResponse::class.java)

                            if (fcmResponse != null){
                                Log.e("offlineUser", fcmResponse.toString())

                                socketListener.offlineUser(fcmResponse.result)
                            }


                        }
                    }

                }
                .on("oneToOneChat") { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty()) {
                            println("oneToOneChat=" + args.getOrNull(0).toString())
                            try {
                                val gson = Gson()
                                val fcmResponse: OneToOneChatResponse = gson.fromJson(args.getOrNull(0).toString(), OneToOneChatResponse::class.java)
                                Log.e("checkOnlineUser", fcmResponse.toString())
                                fcmResponse.chatHistory.getOrNull(0)
                                    ?.let { socketListener.oneToOneChat(it) }
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                }
                .on("chatHistory") { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty()) {
                            try {
                                val gson = Gson()
                                val fcmResponse: ChatHistoryResponse = gson.fromJson(args.getOrNull(0).toString(), ChatHistoryResponse::class.java)

                                socketListener.chatHistroy(fcmResponse.result)
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                }
                .on("chatList") { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty()) {
                            try {
                                val gson = Gson()
                                val fcmResponse: chatDataList = gson.fromJson(args.getOrNull(0).toString(), chatDataList::class.java)
                                Log.e("chatList", fcmResponse.toString())
                                socketListener.chatListData(fcmResponse.result)
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                }
                .on("viewChat") { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty()) {
                            try {


                                val gson = Gson()
                                val fcmResponse: Responce =
                                    gson.fromJson(args.getOrNull(0).toString(), Responce::class.java)
                                Log.e("viewChat", fcmResponse.toString())
                                socketListener.viewchat(fcmResponse.result.messages)

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                            System.out.println("viewChat=" + args.getOrNull(0))
                        }
                    }

                }
                .on("checkOnlineUser") { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty()) {
                            try {

                                val gson = Gson()
                                val fcmResponse: CheckOnlineUserResponse = gson.fromJson(args.getOrNull(0).toString(), CheckOnlineUserResponse::class.java)
                                println("checkOnlineUser===== ${args.getOrNull(0)}")
                                Log.e("checkOnlineUser", fcmResponse.toString())
                                socketListener.checkOnlineUser(fcmResponse.result)

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                            println("checkOnlineUser=" + args.getOrNull(0))
                        }
                    }

                }
                .on("typing") { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty()) {
                            try {
                                val gson = Gson()
                                val fcmResponse: JsonObject = gson.fromJson(args.getOrNull(0).toString(), JsonObject::class.java)
                                println("checkOnlineUser===== ${args.getOrNull(0)}")
                                Log.e("checkOnlineUser", fcmResponse.toString())
                                socketListener.typing(fcmResponse)

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                            println("checkOnlineUser=" + args.getOrNull(0))
                        }
                    }

                }
                .on("userIsTyping") { args ->
                    Handler(Looper.getMainLooper()).post {
                        if (args != null && args.isNotEmpty()) {
                            try {
                                val gson = Gson()
                                val fcmResponse: UserTypingResponse = gson.fromJson(args.getOrNull(0).toString(), UserTypingResponse::class.java)
                                println("userIsTyping===== ${args.getOrNull(0)}")
                                Log.e("userIsTyping", fcmResponse.toString())
                                socketListener.typingUser(fcmResponse.result)

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                }



        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("browse_page_err---", "" + ex.message)

            Log.d(TAG, ex.toString())
            Log.e("browse_page_err---", "" + ex.toString())
            Log.e("browse_page_err---", "" + ex.message)

        }
    }

    object Messagedata : Emitter.Listener {
        override fun call(vararg args: Any?) {
            println("viewChat=" + "message")
        }

    }

}

object NewMessage : Emitter.Listener {
    override fun call(vararg args: Any?) {
        Handler(Looper.getMainLooper()).post {
            println("viewChat=" + "connection")
        }

    }

}




