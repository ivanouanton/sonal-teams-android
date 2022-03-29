package com.waveneuro.utils;

import android.content.Context;

import com.asif.abase.data.model.APIError;
import com.asif.abase.injection.qualifier.ApplicationContext;
import com.google.gson.Gson;
import com.waveneuro.R;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import timber.log.Timber;

public class ErrorUtil {

    //    @Inject
    Gson gson;

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    public ErrorUtil() {
        gson = new Gson();
    }

    public APIError parseError(Throwable e) {

        APIError apiError = new APIError();

        try {

            if (e instanceof HttpException) {
                ResponseBody errorBody = ((HttpException) e).response().errorBody();
                apiError = gson.fromJson(errorBody.string(), APIError.class);
                if(apiError == null)
                    apiError = new APIError();
                apiError.setCode(String.valueOf(((HttpException) e).code()));
            } else if (e instanceof SocketTimeoutException) {
                apiError.setMessage(context.getString(R.string.socketTimeoutException));
            } else if (e instanceof IOException) {
                apiError.setMessage(context.getString(R.string.networkError));
            } else {
                apiError.setMessage(context.getString(R.string.unknownError));
            }
        } catch (Exception ex) {
            Timber.e("Unknown exception: {%s}", ex.getMessage());
            apiError.setMessage(context.getString(R.string.unknownError));
        }
        return apiError;
    }

    public APIError parseError(Throwable e, String message) {

        APIError apiError = new APIError();

        try {
            if (e instanceof HttpException) {
                ResponseBody errorBody = ((HttpException) e).response().errorBody();
                apiError = gson.fromJson(errorBody.string(), APIError.class);
            } else if (e instanceof SocketTimeoutException) {
                apiError.setMessage(context.getString(R.string.socketTimeoutException));
            } else if (e instanceof IOException) {
                apiError.setMessage(context.getString(R.string.networkError));
            } else {
                apiError.setMessage(message);
            }
        } catch (Exception ex) {
            Timber.e("Unknown exception: {%s}", ex.getMessage());
            apiError.setMessage(context.getString(R.string.unknownError));
        }
//        Crashlytics.log("ERROR 2 :: " + e.getMessage() + " :: " + e.toString());
//        Crashlytics.logException(e);
        if (apiError == null) {
            apiError = new APIError();
        }

        return apiError;
    }
}
