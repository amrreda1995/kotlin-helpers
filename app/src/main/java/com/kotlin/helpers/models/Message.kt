package com.kotlin.helpers.models

import com.google.gson.annotations.SerializedName

data class Message(@SerializedName("message") var errorMessage: String = "", var statusCode: Int = 0)