package me.injent.myschool.core.network.retrofit

import android.content.Context
import android.util.Log
import me.injent.myschool.core.common.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(context: Context) : Interceptor {

    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        Log.d("REQUEST", chain.request().url.toString())

        sessionManager.fetchToken()?.let { token ->
            requestBuilder.addHeader("Access-Token", token)
        }

        return chain.proceed(requestBuilder.build())
    }
}