package com.fernando.billit.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.fernando.billit.databinding.ActivityLoginBinding
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.isNetworkAvailable
import com.fernando.billit.extension.toastMessage
import com.fernando.billit.util.AuthResource
import com.fernando.billit.viewmodel.LoginViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class LoginActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var loadingPopup: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(LoginViewModel::class.java)

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
//            if (isNetworkAvailable())
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

    private fun navToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
