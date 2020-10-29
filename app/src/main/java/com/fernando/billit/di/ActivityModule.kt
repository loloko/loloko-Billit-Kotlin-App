package com.fernando.billit.di

import com.fernando.billit.di.auth.AuthModule
import com.fernando.billit.di.auth.AuthScope
import com.fernando.billit.di.auth.AuthViewModelModule
import com.fernando.billit.di.main.MainFragmentModule
import com.fernando.billit.di.main.MainModule
import com.fernando.billit.di.main.MainScope
import com.fernando.billit.di.main.MainViewModelModule
import com.fernando.billit.ui.auth.ForgotPasswordActivity
import com.fernando.billit.ui.auth.LoginActivity
import com.fernando.billit.ui.auth.RegisterActivity
import com.fernando.billit.ui.main.BillDetailsActivity
import com.fernando.billit.ui.main.FriendActivity
import com.fernando.billit.ui.main.MainActivity
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

    @MainScope
    @ContributesAndroidInjector(modules = [MainViewModelModule::class, MainModule::class])
    abstract fun contributeFriendActivity(): FriendActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainViewModelModule::class, MainModule::class])
    abstract fun contributeBillDetailsActivity(): BillDetailsActivity

}