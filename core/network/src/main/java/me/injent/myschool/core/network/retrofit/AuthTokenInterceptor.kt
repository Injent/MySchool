package me.injent.myschool.core.network.retrofit

import android.util.Log
import me.injent.myschool.auth.SessionManager
import me.injent.myschool.core.network.BuildConfig.DEBUG
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Interceptor to add auth token to requests
 */
class AuthTokenInterceptor @Inject constructor (
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (DEBUG)
            Log.d("API", chain.request().url.toString())

        return try {
            val token = sessionManager.getAccessToken()
            if (token != null) {
                val requestWithHeader = request.newBuilder()
                    .removeHeader("Access-Token")
                    .addHeader("Access-Token", token)
                    .build()
                chain.proceed(requestWithHeader)
            } else {
                chain.proceed(request)
            }
        } catch (e: SocketTimeoutException) {
            if (DEBUG) {
                Log.d("AuthTokenInterceptor", "Error during request to ${request.url}:", e)
            }
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(0)
                .message("Socket timeout")
                .body("{${e}}".toResponseBody(null))
                .build()
        } catch (e: Exception) {
            if (DEBUG) {
                Log.d("AuthTokenInterceptor", "Error during request to ${request.url}:", e)
            }
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(0)
                .message(e.localizedMessage ?: "")
                .body("{${e}}".toResponseBody(null))
                .build()
        }
    }
}