package me.injent.myschool.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        const val ARG_ACCOUNT_TYPE = ""
        const val ARG_AUTH_TOKEN_TYPE = ""
        const val ARG_IS_ADDING_NEW_ACCOUNT = ""
    }
}