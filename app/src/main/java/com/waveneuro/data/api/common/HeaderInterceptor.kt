package com.waveneuro.data.api.common

import com.waveneuro.data.Config
import com.waveneuro.data.preference.PreferenceManager
import com.waveneuro.ui.base.handler.error.model.TokenException
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val prefManager: PreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        with(chain.request().newBuilder()) {

            addHeader("X-Client", "Android")

            val request = chain.request()

            if (!request.url.toUrl().path.contains("login")) {

                addHeader(
                    "Authorization",
                    "Bearer ${prefManager.accessToken}"
                )

                val response = chain.proceed(build())

                if (response.code == UNAUTHORIZED) {
                    response.close()

                    val refreshRequestBuilder = chain.request().newBuilder()

                    val newUrl = HttpUrl.Builder()
                        .scheme(chain.request().url.scheme)
                        .host(chain.request().url.host)
                            //TODO
//                        .addPathSegment(Config.PATHS[0])
                        .addPathSegment("refresh")
                        .build()

                    refreshRequestBuilder.url(newUrl)
                    refreshRequestBuilder.post(FormBody.Builder().build())
                    refreshRequestBuilder.addHeader(
                        "Authorization",
                        "Bearer ${prefManager.refreshToken}"
                    )

                    val refreshResponse = chain.proceed(refreshRequestBuilder.build())

                    Timber.e("REFRESH :: ${refreshResponse.code}")

                    if (refreshResponse.code == UNAUTHORIZED) {
                        throw TokenException()
                    } else {
                        try {
                            val jsonObject = JSONObject(refreshResponse.body?.string() ?: "")
                            if (jsonObject.has("access_token")) {
                                val newToken = jsonObject.getString("access_token")
                                prefManager.accessToken = newToken
                                Timber.e("NEW ACCESS TOKEN :: $newToken")
                                refreshResponse.close()

                                val newRequestBuilder = request.newBuilder()
                                newRequestBuilder.addHeader(
                                    "Authorization",
                                    "Bearer ${prefManager.accessToken}"
                                )

                                return chain.proceed(newRequestBuilder.build())
                            }
                        } catch (e: JSONException) { Timber.e(e) }
                    }
                } else {
                    return response
                }
            }
            return chain.proceed(build())
        }
    }

    companion object {
        private const val UNAUTHORIZED = 401
    }
}