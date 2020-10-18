package com.fernando.billit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.fernando.billit.databinding.ActivityLoginBinding
import com.fernando.billit.viewmodel.LoginViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class LoginActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(LoginViewModel::class.java)

    }

    //go to register activity
    fun btSignUpClick(view: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    //button close action click
    fun btCloseClick(view: View?) {
        finish()
    }

    fun resetPasswordClick(view: View?) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

}
