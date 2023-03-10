package com.waveneuro.ui.base.handler.error.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class AppError

object NoInternetError : AppError()

object FailedRefreshTokenError : AppError()

object NotFound : AppError()

data class UnknownError(val message: String) : AppError()

@JsonClass(generateAdapter = true)
data class ApiError(
    @Json(name = "code") val code: Int?,
    @Json(name = "error") val error: String?,
    @Json(name = "error_type") val errorType: String?,
    @Json(name = "message") val message: String?
) : AppError()

object ErrorMessage {
    // for error
    const val TOS_NOT_SIGNED = "ToS not signed"
    const val BAD_USER_NAME_OR_PASSWORD = "Bad username or password"
    const val INVALID_CODE = "Invalid code"
    // for message
    const val ACTIVE_SONAL_SESSION_NOT_FOUND = "Active Sonal session not found"
}

object ErrorType {
    const val TOS_DOES_NOT_SIGNED = "tos_not_signed"
    const val SESSION_ARE_NOT_AVAILABLE = "no_available_protocol"
}

object ErrorCode {
    const val BAD_REQUEST = 400
    const val NOT_FOUND = 404
    const val NOT_ACCEPTABLE = 406
}