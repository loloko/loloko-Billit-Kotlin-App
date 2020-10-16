package com.fernando.billit.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.fernando.billit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


//
//    @Singleton
//    @Provides
//    fun provideRequestOptionsBookCover(): RequestOptions {
//        return RequestOptions.placeholderOf(R.drawable.ic_no_book_cover).error(R.drawable.ic_no_book_cover)
//    }

//    @Singleton
//    @Provides
//    fun provideGlideInstance(application: Application, requestOptions: RequestOptions): RequestManager {
//        return Glide.with(application).setDefaultRequestOptions(requestOptions)
//    }


}
