package com.example.googlesignincompose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.common.SignInButton

// This is the UI for the sign in screen
// It contains the following fields:
// state: SignInState
// onSignInClick: () -> Unit
// We use LaunchedEffect to show the error message if any
// We use Box to show the sign in button
// We use Button to show the sign in button
// We use Text to show the sign in button text
// We use LocalContext to get the context
// We use Toast to show the error message
// We use Modifier to set the size of the sign in button
// We use Modifier to set the padding of the sign in button
@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onSignInClick) {
            Text(text = "Sign In")
        }

    }

}