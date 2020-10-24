package com.fernando.billit.di

import com.fernando.billit.repository.AuthRepository
import com.fernando.billit.repository.FriendRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, database: FirebaseFirestore): AuthRepository = AuthRepository(firebaseAuth, database)

    @Provides
    fun provideFriendRepository(database: FirebaseFirestore): FriendRepository = FriendRepository(database)
}