package com.fernando.billit.repository

import com.fernando.billit.R
import com.fernando.billit.extension.codeToBase64
import com.fernando.billit.model.UserModel
import com.fernando.billit.util.AuthResource
import com.fernando.billit.util.FirebaseConstants
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepository @Inject constructor(private val auth: FirebaseAuth, private val database: FirebaseFirestore) {

   suspend fun registerUserWithEmail(user: UserModel): AuthResource<UserModel> {

        return try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()

            AuthResource.Authenticated(user)

        } catch (e: FirebaseAuthWeakPasswordException) {
            AuthResource.Error(R.string.weak_password)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResource.Error(R.string.invalid_email)
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthResource.Error(R.string.user_collision)
        } catch (e: Exception) {
            AuthResource.Error(R.string.error_user_register)
        }
    }

    suspend fun insertUserFirebaseDatabase(user: UserModel): Boolean {

        return try {
            database.collection(FirebaseConstants.USER.USERS)
                .document(user.id)
                .set(user.convertToHashMap())
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun signInWithCredentials(credentials: AuthCredential): AuthResource<UserModel> {

        return try {
            auth.signInWithCredential(credentials).await()

            // Get user from auth session
            val user = UserModel()
            user.email = auth.currentUser?.email!!
            user.name = auth.currentUser?.displayName!!
            user.id = user.email.codeToBase64()

            AuthResource.Authenticated(user)

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResource.Error(R.string.invalid_email)
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthResource.Error(R.string.user_collision)
        } catch (e: Exception) {
            AuthResource.Error(R.string.error_sign_in)
        }
    }

    fun isUserAlreadySignIn(): UserModel? {

        return if (auth.currentUser != null) {
            // Get user from auth session and set ID
            val email = auth.currentUser?.email!!
            UserModel(email.codeToBase64())
        } else
            null
    }

    suspend fun sendResetPasswordEmail(email: String): AuthResource<UserModel> {

        return try {
            auth.sendPasswordResetEmail(email).await()

            AuthResource.ResetPassword

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResource.Error(R.string.invalid_email)
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthResource.Error(R.string.invalid_email)
        } catch (e: Exception) {
            AuthResource.Error(R.string.error_send_email)
        }
    }

    suspend fun signInWithEmail(email: String, password: String): AuthResource<UserModel> {

        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            AuthResource.Authenticated(UserModel(email.codeToBase64()))

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResource.Error(R.string.invalid_credentials)
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthResource.Error(R.string.invalid_credentials)
        } catch (e: Exception) {
            AuthResource.Error(R.string.error_sign_in)
        }

    }

    suspend fun getUserById(id: String): DocumentSnapshot? {

        return try {
            database.collection(FirebaseConstants.USER.USERS)
                .document(id)
                .get()
                .await()

        } catch (e: Exception) {
            null
        }
    }
}