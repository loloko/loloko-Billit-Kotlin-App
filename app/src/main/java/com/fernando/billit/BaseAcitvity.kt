package com.fernando.billit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.observe
import com.fernando.billit.extension.TAG
import com.fernando.billit.ui.auth.LoginActivity
import com.fernando.billit.util.AuthResource.*
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        sessionManager.getAuthUser().observe(this
        ) { userAuthResource ->

            when (userAuthResource) {
                is Loading -> {
                    Log.d(TAG, "onChanged: LOADING...")
                }
                is Authenticated -> {
                    Log.d(TAG, "onChanged: AUTHENTICATED... " + userAuthResource.data?.email)
                }
                is Error -> {
                    Log.d(TAG, "onChanged:  ERROR...")
                }
                is NotAuthenticated -> {
                    Log.d(TAG, "onChanged: NOT AUTHENTICATED. Send user to Login screen.")
                    navLoginScreen()
                }
                else -> {

                }
            }
        }
    }

    private fun navLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
