package com.fernando.billit.repository

import android.util.Log
import com.fernando.billit.R
import com.fernando.billit.extension.TAG
import com.fernando.billit.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject


class AuthRepository @Inject constructor(private val auth: FirebaseAuth, private val databaseRef: DatabaseReference) {

    fun registerUserWithEmail(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {



                } else {
                    throw task.exception!!
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "registerUserWithEmail: ${e.message}")
        }
    }

}