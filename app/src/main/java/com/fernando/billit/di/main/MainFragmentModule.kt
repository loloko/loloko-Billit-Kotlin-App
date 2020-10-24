package com.fernando.billit.di.main

import com.fernando.billit.ui.main.ActivitiesFragment
import com.fernando.billit.ui.main.BillFragment
import com.fernando.billit.ui.main.SuggestedDebtsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeActivitiesFragment(): ActivitiesFragment

    @ContributesAndroidInjector
    abstract fun contributeBillFragment(): BillFragment

    @ContributesAndroidInjector
    abstract fun contributeSuggestedDebtsFragment(): SuggestedDebtsFragment
}