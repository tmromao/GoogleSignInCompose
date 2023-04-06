package com.example.googlesignincompose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//  This is the ViewModel for the sign in screen
//  It contains the state of the sign in screen
//  It contains the logic to update the state
//  It contains the logic to reset the state
//  It contains the logic to get the signed in user
class SignInViewModel : ViewModel() {

    // This is the state of the sign in screen
    // It contains the following fields:
    // isSignInSuccessful: Boolean
    // signInError: String?
    // We use MutableStateFlow to update the state
    // We use StateFlow to expose the state to the UI
    // We use asStateFlow to convert MutableStateFlow to StateFlow
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    // This is called when the user clicks on the sign in button
    // We update the state to show the sign in progress bar
    // This will hide the sign out button and show the sign in button
    fun onSignInResult(result: SignInResult) {
        _state.update { state ->
            state.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    // This is called when the user clicks on the sign out button
    // We reset the state to the initial state
    // This will hide the sign out button and show the sign in button
    // This will also hide the error message if any
    // This will also hide the user details if any
    // This will also hide the sign in progress bar
    // This will also hide the sign out progress bar

    fun resetState() {
        _state.update { SignInState() }
    }
}