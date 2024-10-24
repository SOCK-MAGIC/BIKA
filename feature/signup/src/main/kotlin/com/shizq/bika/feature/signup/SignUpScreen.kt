package com.shizq.bika.feature.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SignInScreen(component: SignUpComponent) {
    SignUpContent()
}

@Composable
internal fun SignUpContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize(),
    ) {
        var name by remember { mutableStateOf("") }
        OutlinedTextField(name, {}, placeholder = { Text("") })
    }
}

@Preview
@Composable
private fun PreviewSignUpScreen() {
    SignInScreen(PreviewSignUpComponent())
}
