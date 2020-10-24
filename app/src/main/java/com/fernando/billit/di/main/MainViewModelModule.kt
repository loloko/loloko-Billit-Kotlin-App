package com.fernando.billit.di.main

import androidx.lifecycle.ViewModel
import com.fernando.billit.di.ViewModelKey
import com.fernando.billit.viewmodel.FriendViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FriendViewModel::class)
    abstract fun bindFriendViewModel(viewModel: FriendViewModel): ViewModel

}