package com.callisdairy.di


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import com.callisdairy.R
import com.callisdairy.UI.Activities.subscription.SubscriptionManageActivity
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.Api_Interface
import com.callisdairy.api.Constants.BASE_URL
import com.callisdairy.api.OtherApi.Location_Interface
import com.callisdairy.extension.setSafeOnClickListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalisModule {

    private var isToastDisplayed = false


    @Provides
    @Singleton
    fun provideMyApi(@ApplicationContext context: Context): Api_Interface {
        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()

            .callTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    val responseBody = response.body.string()
                    val saltHeaderValue = response.headers["X-SESSID"]
                    if (saltHeaderValue != null){
                        SavedPrefManager.saveStringPreferences(context,SavedPrefManager.DYNAMIC_SALT,saltHeaderValue)
                    }

                    try {


                        val json = JSONObject(responseBody)
                        val code = json.getInt("responseCode")

                        if (code == 407) {
                            test(context)
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }


                    response.newBuilder()
                        .body(responseBody.toResponseBody(response.body.contentType()))
                        .build()
                })
                .build()

        val retrofitInstance by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Api_Interface::class.java)
        }
        return retrofitInstance
    }

    @Provides
    @Singleton
    fun provideLocationApi(): Location_Interface {
        val client: OkHttpClient = OkHttpClient.Builder()

            .callTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()

        val retrofitInstance by lazy {
            Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(Location_Interface::class.java)
        }
        return retrofitInstance
    }


    private fun test(context: Context) {
        val intent = Intent(context, SubscriptionManageActivity::class.java)
        intent.putExtra("expired", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }






}
