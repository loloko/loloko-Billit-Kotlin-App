package com.fernando.billit.di

import com.fernando.billit.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, databaseRef: DatabaseReference): AuthRepository = AuthRepository(firebaseAuth, databaseRef)
}