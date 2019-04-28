package com.kotlin.helpers.managers

import android.content.res.Resources
import com.google.gson.Gson
import com.kotlin.helpers.R
import com.kotlin.helpers.models.Message
import com.kotlin.helpers.models.ResponseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

interface ApiRequestManagerInterface {
    fun <T : Any> execute(
        apiRequest: suspend () -> Response<T>,
        onSuccess: ((data: T) -> Unit)? = null,
        onFailure: ((errorMessage: String) -> Unit)? = null,
        finally: (() -> Unit)? = null
    )
}

class ApiRequestManager(private val resources: Resources) : ApiRequestManagerInterface {
    override fun <T : Any> execute(
        apiRequest: suspend () -> Response<T>,
        onSuccess: ((data: T) -> Unit)?,
        onFailure: ((errorMessage: String) -> Unit)?,
        finally: (() -> Unit)?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = verifyResponse(apiRequest.invoke())

                withContext(Dispatchers.Main) {
                    when (result) {
                        is ResponseResult.Success -> onSuccess?.invoke(result.data)
                        is ResponseResult.Failure -> onFailure?.invoke(result.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure?.invoke(resources.getString(R.string.error_while_fetching_data))
                }
            } finally {
                withContext(Dispatchers.Main) {
                    finally?.invoke()
                }
            }
        }
    }

    private fun <T : Any> verifyResponse(response: Response<T>): ResponseResult<T> {
        return try {
            if (response.isSuccessful) {
                if (response.code() == 204) {
                    @Suppress("UNCHECKED_CAST")
                    (ResponseResult.Success(response.raw().message() as T))
                } else {
                    ResponseResult.Success(response.body()!!)
                }
            } else {
                ResponseResult.Failure(
                    Gson().fromJson(
                        response.errorBody()?.string(),
                        Message::class.java
                    ).message
                )
            }
        } catch (ex: Exception) {
            ResponseResult.Failure(ex.localizedMessage)
        }
    }
}