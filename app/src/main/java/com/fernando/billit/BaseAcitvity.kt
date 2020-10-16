package com.fernando.billit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.fernando.billit.extension.TAG
import com.fernando.billit.ui.LoginActivity
import com.fernando.billit.util.AuthResource.AuthStatus.*
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
        sessionManager.getAuthUser()!!.observe(this,
            { userAuthResource ->
                if (userAuthResource != null) {
                    when (userAuthResource.status) {
                        LOADING -> {
                            Log.d(TAG, "onChanged: LOADING...")
                        }
                        AUTHENTICATED -> {
                            Log.d(TAG, "onChanged: AUTHENTICATED... " + userAuthResource.data?.email)
                        }
                        ERROR -> {
                            Log.d(TAG, "onChanged:  ERROR...")
                        }
                        NOT_AUTHENTICATED -> {
                            Log.d(TAG, "onChanged: NOT AUTHENTICATED. Send user to Login screen.")
                            navLoginScreen()
                        }
                    }
                }
            })
    }

    private fun navLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
