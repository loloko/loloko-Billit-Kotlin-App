package com.fernando.billit.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
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
