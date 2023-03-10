package com.waveneuro.ui.base.handler.error

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.waveneuro.ui.base.handler.error.model.*
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

class ErrorHandlerImpl(
    moshi: Moshi
) : ErrorHandler {

    private val appErrorAdapter: JsonAdapter<ApiError> = moshi.adapter(ApiError::class.java)

    override fun handle(throwable: Throwable): AppError {

        if (throwable !is CancellationException) {
            Timber.e(throwable)
        }

        return when (throwable) {
            is HttpException -> {
                var errorString = "Unknown error"
                throwable.response()?.errorBody()?.let {
                    errorString = it.string()
                    return try {
                        when (throwable.code()) {
                            HttpURLConnection.HTTP_BAD_REQUEST -> {
                                val apiError = appErrorAdapter.fromJson(errorString)!!

                                apiError
                            }
                            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                                val apiError = appErrorAdapter.fromJson(errorString)!!

                                apiError
                            }
                            HttpURLConnection.HTTP_NOT_FOUND -> NotFound
                            HttpURLConnection.HTTP_NOT_ACCEPTABLE -> {
                                val apiError = appErrorAdapter.fromJson(errorString)!!

                                apiError
                            }
                            else -> UnknownError(errorString)
                        }
                    } catch (e: Exception) {
                        UnknownError(e.toString())
                    }
                }

                UnknownError(errorString)
            }
            is TokenException -> {
                FailedRefreshTokenError
            }
            is UnknownHostException, is SocketTimeoutException -> {
                NoInternetError
            }
            is CancellationException -> throw throwable
            else -> UnknownError(throwable.toString())
        }
    }

}
