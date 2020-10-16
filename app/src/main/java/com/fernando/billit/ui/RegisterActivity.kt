package com.fernando.billit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.fernando.billit.databinding.ActivityRegisterBinding
import com.fernando.billit.viewmodel.RegisterViewModel
import com.fernando.billit.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class RegisterActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this, providerFactory).get(RegisterViewModel::class.java)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        navToLogin()
    }

    private fun navToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun btRegisterClick(view: View?) {

        viewModel.registerUser()

    }

}
