package com.kotlin.helpers.managers

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kotlin.helpers.enums.LanguageCode
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

interface RetrofitClientManagerInterface {
    val client: Retrofit
    val httpClient: OkHttpClient.Builder
}

class RetrofitClientManager : RetrofitClientManagerInterface {
    override val httpClient: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor { chain: Interceptor.Chain ->
        var language = LanguageCode.EN.code

        if (Locale.getDefault().language == LanguageCode.AR.code) language = LanguageCode.AR.code

        val request = chain.request().newBuilder()
            .addHeader("Accept-Language", language)
            .addHeader("Content-Type", "application/json")
            .build()

        chain.proceed(request)
    }

    override val client: Retrofit = Retrofit.Builder()
        .baseUrl("")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(httpClient.build())
        .build()
}