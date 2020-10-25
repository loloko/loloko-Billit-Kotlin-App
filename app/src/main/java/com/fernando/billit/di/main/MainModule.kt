package com.fernando.billit.di.main

import com.fernando.billit.adapter.FriendAdapter
import dagger.Module
import dagger.Provides


@Module
class MainModule {

    @MainScope
    @Provides
    fun provideFriendAdapter(): FriendAdapter = FriendAdapter()



}