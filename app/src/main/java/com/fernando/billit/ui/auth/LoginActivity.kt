package com.fernando.billit.ui.auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fernando.billit.R
import com.fernando.billit.databinding.ActivityLoginBinding
import com.fernando.billit.extension.createLoadingPopup
import com.fernando.billit.extension.isNetworkAvailable
import com.fernando.billit.extension.toastMessage
import com.fernando.billit.ui.main.MainActivity
import com.fernando.billit.util.AuthResource.*
import com.fernando.billit.viewmodel.LoginViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class LoginActivity : DaggerAppCompatActivity() {
    companion object {
        private const val GOOGLE_SIGN_IN = 1158
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var loadingPopup: Dialog

    // Facebook sign in
    private val mCallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    // Google sign in
    private val mGoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        GoogleSignIn.getClient(this, gso)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(LoginViewModel::class.java)

        // Create loading dialog
        loadingPopup = createLoadingPopup()

        componentsActionsInit()
        subscribeObservers()
    }

    override fun onStart() {
        super.onStart()

        //check if exist a logged user
        viewModel.isUserAlreadySignIn()
    }

    private fun componentsActionsInit() {
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
                signInWithFacebook()
        }

        // Button Sign in with Google
        binding.btSignInWithGoogle.setOnClickListener {
            if (isNetworkAvailable())
                signInWithGoogle()
        }
    }

    private fun subscribeObservers() {
        viewModel.userResultObserver().observe(this) { user ->
            when (user) {
                is Loading -> {
                    loadingPopup.show()
                }
                is Authenticated -> {
                    loadingPopup.dismiss()
                    navToMainScreen()
                }
                is Error -> {
                    toastMessage(user.message, isWarning = true)
                    loadingPopup.dismiss()
                }
                is NotAuthenticated -> {
                    loadingPopup.dismiss()
                }
                else -> loadingPopup.dismiss()
            }
        }
    }

    private fun signInWithFacebook() {

        val loginManager = LoginManager.getInstance()

        loginManager.logInWithReadPermissions(this, listOf("email", "public_profile"))
        loginManager.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                viewModel.signInWithCredentials(credential)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
                this@LoginActivity.toastMessage(R.string.facebook_failed, isWarning = true)
            }
        })
    }

    private fun signInWithGoogle() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)

                viewModel.signInWithCredentials(credential)

            } catch (e: ApiException) {
                toastMessage(R.string.google_failed, isWarning = true)
            }
            return
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun navToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
