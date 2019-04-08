package com.kotlin.helpers.managers

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope

enum class GoogleAccountInfo {
    ID, NAME, PROFILE_PICTURE, EMAIL, AUTH_TOKEN, ID_TOKEN
}

interface GoogleAuthManagerInterface {
    fun signIn(): Intent

    fun checkIfAccountSignedInBefore(): Boolean

    fun signOut()

    suspend fun getAccountData(result: Intent?, googleAccountInfo: GoogleAccountInfo): String?
}

class GoogleAuthManager(private val context: Context) : GoogleAuthManagerInterface {
    private val googleSignInAuth: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestIdToken("")
            .requestServerAuthCode("")
            .build()

    private val googleSignInClient: GoogleSignInClient =
        GoogleSignIn.getClient(context, googleSignInAuth)

    override fun signIn(): Intent {
        signOut()

        return googleSignInClient.signInIntent
    }

    override fun checkIfAccountSignedInBefore(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(context) != null
    }

    override suspend fun getAccountData(result: Intent?, googleAccountInfo: GoogleAccountInfo): String? {
        val googleSignInResult: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(result)

        return when (googleAccountInfo) {
            GoogleAccountInfo.ID -> googleSignInResult.signInAccount?.id

            GoogleAccountInfo.NAME -> googleSignInResult.signInAccount?.displayName

            GoogleAccountInfo.PROFILE_PICTURE -> googleSignInResult.signInAccount?.photoUrl.toString()

            GoogleAccountInfo.EMAIL -> googleSignInResult.signInAccount?.email

            GoogleAccountInfo.AUTH_TOKEN -> getAuthToken(googleSignInResult.signInAccount?.account)

            else -> googleSignInResult.signInAccount?.idToken
        }
    }

    private fun getAuthToken(account: Account?): String? {
        var authToken: String? = null

        try {
            val am = AccountManager.get(context)

//            val accounts = am.getAccountsByType("com.google")
//            Log.e("size", accounts.size.toString())

            val accountManagerFuture: AccountManagerFuture<Bundle>

            accountManagerFuture = am.getAuthToken(
                account,
                "oauth2:https://www.googleapis.com/auth/userinfo.profile",
                null,
                context as Activity,
                null,
                null
            )
            val authTokenBundle = accountManagerFuture.result
            authToken = authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN)!!.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return authToken
    }

    override fun signOut() {
        googleSignInClient.signOut()
    }

}