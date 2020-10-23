package com.fernando.billit.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fernando.billit.R
import com.fernando.billit.databinding.ActivityLoginBinding
import com.fernando.billit.extension.TAG
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.isNetworkAvailable
import com.fernando.billit.extension.toastMessage
import com.fernando.billit.util.AuthResource
import com.fernando.billit.viewmodel.LoginViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class LoginActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var loadingPopup: Dialog
    private lateinit var mCallbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(LoginViewModel::class.java)

        //Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create()
        // Create loading dialog
        loadingPopup = createLoadingPopup()


        initComponentsActions()
        observers()
    }

    private fun initComponentsActions() {
        // Close App
        binding.imClose.setOnClickListener {
            finish()
        }

        // Nav user to Forgot password Screen
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        // Nav user to Register screen
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        // Button Sign in with email and password
        binding.btSignInWithEmail.setOnClickListener {
            if (isNetworkAvailable())
                viewModel.signInWithEmail(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        // Button Sign in with Facebook
        binding.btSignInWithFacebook.setOnClickListener {
            if (isNetworkAvailable())
                facebookLogin()
        }

        // Button Sign in with Google
        binding.btSignInWithGoogle.setOnClickListener {
//            if (isNetworkAvailable())
        }
    }

    private fun observers() {
        viewModel.userResultObserver().observe(this, { user ->
            if (user != null) {
                when (user.status) {
                    AuthResource.AuthStatus.LOADING -> {
                        loadingPopup.show()
                    }
                    AuthResource.AuthStatus.AUTHENTICATED -> {
                        loadingPopup.dismiss()
                        navToMainScreen()
                    }
                    AuthResource.AuthStatus.ERROR -> {
                        toastMessage(user.message, isWarning = true)
                        loadingPopup.dismiss()
                    }
                    AuthResource.AuthStatus.NOT_AUTHENTICATED -> {
                        loadingPopup.dismiss()
                    }
                    else -> loadingPopup.dismiss()
                }
            }
        })
    }

    private fun facebookLogin() {

        val loginManager = LoginManager.getInstance()

        loginManager.logInWithReadPermissions(this, listOf("email", "public_profile"))
        loginManager.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                viewModel.signInWithFacebook(credential)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
                this@LoginActivity.toastMessage(R.string.facebook_failed, isWarning = true)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }


    private fun navToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
