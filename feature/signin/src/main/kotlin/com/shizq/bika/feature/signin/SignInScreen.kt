package com.shizq.bika.feature.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SignInScreen(component: SignInComponent) {
    val credentialState by component.credentialState.collectAsStateWithLifecycle()
    SignInContent(
        signIn = component::signIn,
        credentialState = credentialState,
        updateEmail = component::updateEmail,
        updatePassword = component::updatePassword,
    )
}

@Composable
internal fun SignInContent(
    signIn: () -> Unit,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    credentialState: CredentialState,
    modifier: Modifier = Modifier,
) {
    when (credentialState) {
        CredentialState.Loading -> Unit
        is CredentialState.Success -> {
            Scaffold { innerPadding ->
                Column(
                    modifier
                        .padding(innerPadding)
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxSize(),
                ) {
                    var passwordVisible by rememberSaveable { mutableStateOf(false) }
                    val localFocusManager = LocalFocusManager.current
                    OutlinedTextField(
                        value = credentialState.userCredential.email,
                        onValueChange = updateEmail,
                        label = { Text("账号") },
                        singleLine = true,
                        placeholder = { Text("账号") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { localFocusManager.moveFocus(FocusDirection.Down) },
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = credentialState.userCredential.password,
                        onValueChange = updatePassword,
                        label = { Text("密码") },
                        singleLine = true,
                        placeholder = { Text("密码") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        trailingIcon = {
                            val image = if (passwordVisible) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            }

                            val description =
                                if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(image, description)
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                signIn()
                            },
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    TextButton({}) {
                        Text("注册账号")
                    }
                    TextButton({}) {
                        Text("忘记密码(接口暂时不可用)")
                    }
                    Spacer(Modifier.weight(1f))
                    ExtendedFloatingActionButton(
                        onClick = {
                            signIn()
                        },
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text("登录", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
