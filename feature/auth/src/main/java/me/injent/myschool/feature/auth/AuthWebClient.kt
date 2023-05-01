package me.injent.myschool.feature.auth

import android.util.Log
import android.webkit.*
import kotlinx.coroutines.*
import me.injent.myschool.feature.auth.BuildConfig.DEBUG


internal class AuthWebClient(
    private val onTokenAcquired: (String) -> Unit,
    private val onError: (message: String) -> Unit,
    private val login: String,
    private val password: String
) : WebViewClient() {

    private var tokenReceived = false
    private var pageLoaded = false
    private var gosLoginSkipped = false
    private var errorChecked = false
    private var timeoutJob: Job? = null

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
        handleTokenUrl(webView, url)
        return super.shouldOverrideUrlLoading(webView, url)
    }

    override fun shouldOverrideUrlLoading(
        webView: WebView,
        request: WebResourceRequest
    ): Boolean {
        handleTokenUrl(webView, request.url.toString())
        return super.shouldOverrideUrlLoading(webView, request)
    }

    override fun onPageFinished(webView: WebView, url: String) {
        if (!tokenReceived) {
            timeoutJob = CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
                delay(10_000)
                if (!errorChecked && !tokenReceived && isActive) {
                    errorChecked = true
                    onError("Unknown error")
                    withContext(Dispatchers.Main) {
                        webView.evaluateJavascript(
                            "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();"
                        ) { html ->
                            if (DEBUG) {
                                Log.d("HTML", html!!)
                            }
                        }
                        clearData(webView)
                    }
                }
                cancel()
            }
        }
        if (!pageLoaded) {
            inputData(webView, login, password)
            pageLoaded = true
        }
        if (url.contains("message") && !gosLoginSkipped) {
            skipGosUslugiWarning(webView)
            gosLoginSkipped = true
        }
        if (!errorChecked) {
            val errorCheckScript = """
                (function() {
                    var hint = document.querySelector("body > div > div > div > div > div > form > div.login__body > div.login__body__hint.login__body__hint_error-message > div");
                    console.log(hint.innerHTML);
                    return hint.innerHTML;
                })();
            """.trimIndent()
            webView.evaluateJavascript(errorCheckScript) { result ->
                val message = result
                    .replace("null", "")
                    .replace("\\n", "")
                    .replace("\"", "")
                    .trim()
                if (message.isNotEmpty()) {
                    errorChecked = true
                    onError(message)
                }
            }
        }
    }

    private fun skipGosUslugiWarning(webView: WebView) {
        val skipMessageScript = """
                (function() {
                    var link = document.querySelector("#login_notification_container > div.login-notification_link-container > a");
                    link.click();
                })();
            """.trimIndent()
        webView.evaluateJavascript(skipMessageScript, null)
    }

    private fun inputData(webView: WebView, login: String, password: String) {
        val autoInputScript = """
            (function() {
                var loginElement = document.getElementsByName("login");
                var loginField = loginElement[0];
                loginField.value = '$login';
        
                var passwordElement = document.getElementsByName("password");
                var passwordField = passwordElement[0];
                passwordField.value = '$password';
        
                var buttons = document.getElementsByTagName('input');
                for(var i = 0; i < buttons.length; i++) {
                   if(buttons[i].type == 'submit') {
                       buttons[i].click();
                       break;
                   }
                }
            })();
        """.trimIndent()
        webView.evaluateJavascript(autoInputScript, null)
    }

    private fun handleTokenUrl(webView: WebView, url: String) {
        if (tokenReceived) return
        val param = "#access_token="
        if (url.contains(param)) {
            timeoutJob?.cancel()
            val token = url
                .substringAfter(param)
                .substringBefore("&state=")
            Log.e("TOKEN", token)
            onTokenAcquired(token)
            tokenReceived = true
            clearData(webView)
        }
    }

    private fun clearData(webView: WebView) {
        with(webView) {
            clearCache(true)
            clearHistory()
            clearMatches()
            clearSslPreferences()
            destroy()
        }
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        WebStorage.getInstance().deleteAllData()
    }
}