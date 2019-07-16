package com.kotlin.helpers.retrofit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.lang.Exception

/**
 * By adding this class as an interceptor for OkHttpClient.Builder, this will help you to debug the
 * response which includes status code, response body in the logcat of
 * the AndroidStudio using "interceptor" key in Debug category
 * @Caution Don't use this class in release mode of the application, you really don't want any one
 * inspecting how your entire application works.
 * */

class ResponseLoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val responseString = response.peekBody(Long.MAX_VALUE).string()

        var responseLogTxt = " \nStatus code -> ${response.code()}\n"

        response.header("X-TotalPages")?.let {
            responseLogTxt += "X-TotalPages -> $it\n"
        }

        responseLogTxt += if (responseString.isNotEmpty()) {
            try {
                val `object` = JSONTokener(responseString).nextValue()
                val jsonLog = if (`object` is JSONObject)
                    `object`.toString(4)
                else
                    (`object` as JSONArray).toString(4)

                "Response body -> \n$jsonLog\n"
            } catch (ex: Exception) {
                "Response body -> \n${ex.message}\n"
            }
        } else {
            "No response\n"
        }

        Log.d("interceptor", "$responseLogTxt\n=========================================================================================================\n ")

        return response
    }
}