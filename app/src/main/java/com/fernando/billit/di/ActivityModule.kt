package com.fernando.billit.di

import com.fernando.billit.di.auth.AuthModule
import com.fernando.billit.di.auth.AuthScope
import com.fernando.billit.di.auth.AuthViewModelModule
import com.fernando.billit.di.main.*
import com.fernando.billit.ui.LoginActivity
import com.fernando.billit.ui.MainActivity
import com.fernando.billit.ui.RegisterActivity
import com.fernando.billit.ui.ForgotPasswordActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @AuthScope
    @ContributesAndroidInjector(modules = [AuthViewModelModule::class, AuthModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

    @AuthScope
    @ContributesAndroidInjector(modules = [AuthViewModelModule::class, AuthModule::class])
    abstract fun contributeRegisterActivity(): RegisterActivity

    @AuthScope
    @ContributesAndroidInjector(modules = [AuthViewModelModule::class, AuthModule::class])
    abstract fun contributeForgotPasswordActivity(): ForgotPasswordActivity


    @MainScope
    @ContributesAndroidInjector(modules = [MainFragmentModule::class, MainViewModelModule::class, MainModule::class])
    abstract fun contributeMainActivity(): MainActivity


}