package com.kotlin.helpers.retrofit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.Charset

/*
 * Copyright (C) 2016 Jeff Gilfelt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Edited by @terrakok
 * https://gist.github.com/terrakok/8e435c1473da4c21e1f4299a21e5e6e9
 */

/**
 * By adding this class as an interceptor for OkHttpClient.Builder, this will help you to debug the
 * request which includes headers, data(fields or body) or even the request method in the logcat of
 * the AndroidStudio using "interceptor" key in Debug category
 * @Caution Don't use this class in release mode of the application, you really don't want any one
 * inspecting how your entire application works.
 * */

class CurlLoggingInterceptor : Interceptor {

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var curlCmd = " \n -X ${request.method()}\n"

        val headers = request.headers()
        for (i in 0 until headers.size()) {
            val name = headers.name(i)
            var value = headers.value(i)

            val start = 0
            val end = value.length - 1
            if (value[start] == '"' && value[end] == '"') {
                value = "\\\"" + value.substring(1, end) + "\\\""
            }

            curlCmd += " -H \"$name: $value\"\n"
        }

        request.body()?.let {
            val buffer = Buffer().apply { it.writeTo(this) }
            val charset = it.contentType()?.charset(UTF8) ?: UTF8
            curlCmd += " --data $'" + buffer.readString(charset).replace("\n", "\\n") + "'\n"
        }

        Log.d("interceptor", " \n ${request.url()}${URLDecoder.decode(curlCmd, "UTF-8")} ")

        return chain.proceed(request)
    }
}