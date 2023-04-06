import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CancellationException

// This is the UI client for the sign in screen
// It contains the following fields:
// context: Context
// oneTapClient: SignInClient
// We use Firebase to get the Firebase auth instance
// We use buildSignInRequest to build the sign in request
// We use beginSignIn to begin the sign in process
// We use getSignInCredentialFromIntent to get the sign in credential from the intent
// We use GoogleAuthProvider to get the Google auth credential
// We use signInWithCredential to sign in with the Google auth credential
// We use IntentSender to get the intent sender
// We use UserData to get the user data
// We use SignInResult to get the sign in result
// We use SignInState to get the sign in state

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {

    //  This is the Firebase auth instance
    //  We use this to sign in with the Google auth credential
    private val auth = Firebase.auth

    //  This is called when the user clicks on the sign in button
    //  We begin the sign in process
    //  We return the intent sender
    //  We use IntentSender to get the intent sender
    //  We use buildSignInRequest to build the sign in request
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    //  This is called when the user clicks on the sign out button
    //  We sign out the user
    //  We return the sign out result
    suspend fun getSignInResultFromIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(data = null, e.message)
        }
    }

    //  This is called when the user clicks on the sign out button
    //  We sign out the user

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.Builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerCliendId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

}