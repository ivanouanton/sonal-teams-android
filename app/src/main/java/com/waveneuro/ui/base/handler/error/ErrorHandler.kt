package com.waveneuro.ui.base.handler.error

import com.waveneuro.ui.base.handler.error.model.AppError

interface ErrorHandler {

    fun handle(throwable: Throwable): AppError

}
