package com.fernando.billit.repository

import com.fernando.billit.R
import com.fernando.billit.model.UserModel
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.FirebaseConstants
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepository @Inject constructor(private val auth: FirebaseAuth, private val databaseRef: DatabaseReference) {

    suspend fun registerUserWithEmail(user: UserModel): AuthResource<UserModel> {

        var result = AuthResource.loading<UserModel>()

        try {

            auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    result = AuthResource.authenticated(user)

                }
            }.await()

        } catch (e: FirebaseAuthWeakPasswordException) {
            result = AuthResource.error(R.string.weak_password)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            result = AuthResource.error(R.string.invalid_email)
        } catch (e: FirebaseAuthUserCollisionException) {
            result = AuthResource.error(R.string.user_collision)
        } catch (e: Exception) {
            result = AuthResource.error(R.string.error_user_register)
        }

        return result
    }

    suspend fun insertUserFirebaseDatabase(user: UserModel) {
        databaseRef.child(FirebaseConstants.USER.USERS).child(user.id).setValue(user).await()
    }

    suspend fun sendResetPasswordEmail(email: String): AuthResource<UserModel> {
        var result = AuthResource.loading<UserModel>()

        try {
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    result = AuthResource.resetPassword()
                }
            }.await()

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            result = AuthResource.error(R.string.invalid_email)
        } catch (e: FirebaseAuthInvalidUserException) {
            result = AuthResource.error(R.string.invalid_email)
        } catch (e: Exception) {
            result = AuthResource.error(R.string.error_send_email)
        }

        return result
    }

}