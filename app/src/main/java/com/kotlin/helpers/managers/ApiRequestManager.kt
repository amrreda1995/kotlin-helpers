package com.kotlin.helpers.managers

import android.content.res.Resources
import com.google.gson.Gson
import com.kotlin.helpers.R
import com.kotlin.helpers.models.Message
import com.kotlin.helpers.models.ResponseResult
import kotlinx.coroutines.*
import okhttp3.Headers
import retrofit2.Response

interface ApiRequestManagerInterface {
    fun <T> execute(
        request: suspend () -> Response<T>,
        onSuccess: ((T, Headers) -> Unit)? = null,
        onFailure: ((Message) -> Unit)? = null,
        finally: (() -> Unit)? = null
    ): Job
}

class ApiRequestManager(private val resources: Resources) : ApiRequestManagerInterface {

    override fun <T> execute(
        request: suspend () -> Response<T>,
        onSuccess: ((T, Headers) -> Unit)?,
        onFailure: ((Message) -> Unit)?,
        finally: (() -> Unit)?
    ): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = request.invoke()
                val result = verifyResponse(response)

                withContext(Dispatchers.Main) {
                    when (result) {
                        is ResponseResult.Success -> onSuccess?.invoke(result.data, response.headers())
                        is ResponseResult.Failure -> onFailure?.invoke(result.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure?.invoke(Message(resources.getString(R.string.something_went_wrong)))
                }
            } finally {
                withContext(Dispatchers.Main) {
                    finally?.invoke()
                }
            }
        }
    }

    private fun <T> verifyResponse(response: Response<T>): ResponseResult<T> {
        return try {
            if (response.isSuccessful) {
                if (response.code() == 204) {
                    @Suppress("UNCHECKED_CAST")
                    (ResponseResult.Success(response.raw().message() as T))
                } else {
                    ResponseResult.Success(response.body()!!)
                }
            } else {
                val message = Gson().fromJson(response.errorBody()?.string(), Message::class.java)
                message.statusCode = response.code()
                ResponseResult.Failure(message)
            }
        } catch (ex: Exception) {
            ResponseResult.Failure(Message(ex.localizedMessage))
        }
    }
}