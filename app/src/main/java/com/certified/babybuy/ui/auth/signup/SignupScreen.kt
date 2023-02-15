package com.certified.babybuy.ui.auth.signup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.certified.babybuy.BuildConfig
import com.certified.babybuy.R
import com.certified.babybuy.data.model.User
import com.certified.babybuy.navigation.Screen
import com.certified.babybuy.ui.custom_component.*
import com.certified.babybuy.ui.theme.*
import com.certified.babybuy.util.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.intuit.sdp.R as sdpR
import com.intuit.ssp.R as sspR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {

    val viewModel = hiltViewModel<SignupViewModel>()
    val auth = Firebase.auth

    var name by remember { mutableStateOf("") }
    var isNameError by rememberSaveable { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var isEmailError by rememberSaveable { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordError by rememberSaveable { mutableStateOf(false) }
    var openDialog by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val oneTapState = rememberOneTapSignInState()

    var isLoading by rememberSaveable { mutableStateOf(false) }
    viewModel.uiState.collectAsState().value.let {
        isLoading = it == UIState.LOADING
    }

    var success by rememberSaveable { mutableStateOf(false) }
    viewModel.success.collectAsState().value.let {
        success = it
    }

    var uploadSuccess by rememberSaveable { mutableStateOf(false) }
    viewModel.uploadSuccess.collectAsState().value.let {
        uploadSuccess = it
    }

    var message by rememberSaveable { mutableStateOf("") }
    viewModel.message.collectAsState().value.let {
        it?.let { it1 -> message = it1 }
    }

    if (message.isNotBlank()) {
        LaunchedEffect(true) {
            viewModel._message.value = ""
        }
    }

    if (success) {
        LaunchedEffect(true) {
            viewModel._success.value = false
        }
        val currentUser = auth.currentUser!!
        val user = User(
            uid = currentUser.uid,
            name = name,
            email = currentUser.email.toString(),
            image = currentUser.photoUrl?.toString()
        )
        viewModel.uploadDetails(user)
    }

    if (uploadSuccess) {
        LaunchedEffect(true) {
            viewModel._uploadSuccess.value = false
        }
        val profileChangeRequest =
            userProfileChangeRequest { if (name.isNotBlank()) displayName = name }
        auth.currentUser!!.apply {
            updateProfile(profileChangeRequest)
            sendEmailVerification()
        }
        openDialog = true
    }

    Surface {
        Column(
            modifier = Modifier
                .scrollable(state = scrollState, orientation = Orientation.Vertical)
                .background(color = if (isSystemInDarkTheme()) Surface1Dark else Surface1)
                .padding(
                    top = dimensionResource(id = sdpR.dimen._24sdp).value.dp,
                    start = dimensionResource(id = sdpR.dimen._16sdp).value.dp,
                    end = dimensionResource(id = sdpR.dimen._16sdp).value.dp
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.create_account),
                fontSize = dimensionResource(id = sspR.dimen._16ssp).value.sp,
                color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                fontWeight = FontWeight.SemiBold,
                fontFamily = SpaceGrotesk,
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._8sdp).value.dp)
            )

            Text(
                text = stringResource(id = R.string.create_account_summary),
                fontSize = dimensionResource(id = sspR.dimen._13ssp).value.sp,
                color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                fontWeight = FontWeight.Normal,
                fontFamily = SpaceGrotesk,
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._10sdp).value.dp)
            )

            Text(
                text = stringResource(id = R.string.choose_display_name),
                fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                fontWeight = FontWeight.SemiBold,
                fontFamily = SpaceGrotesk,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._4sdp).value.dp)
            )

            CustomOutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    isNameError = it.isBlank()
                },
                placeholder = stringResource(id = R.string.name_hint),
                errorText = "Name is required *",
                isError = isNameError,
                keyboardActions = KeyboardActions { isNameError = name.isBlank() },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                ),
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._8sdp).value.dp)
            )

            Text(
                text = stringResource(id = R.string.enter_email),
                fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                fontWeight = FontWeight.SemiBold,
                fontFamily = SpaceGrotesk,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._4sdp).value.dp)
            )

            CustomOutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isEmailError = it.isBlank()
                },
                placeholder = stringResource(id = R.string.email_hint),
                errorText = "Email is required *",
                isError = isEmailError,
                keyboardActions = KeyboardActions { isEmailError = email.isBlank() },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
                ),
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._8sdp).value.dp)
            )

            Text(
                text = stringResource(id = R.string.password),
                fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                fontWeight = FontWeight.SemiBold,
                fontFamily = SpaceGrotesk,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._4sdp).value.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordError = it.isBlank()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = if (isSystemInDarkTheme()) SecondaryContainerDark else SecondaryContainer),
                textStyle = TextStyle(
                    color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                    fontFamily = SpaceGrotesk,
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(
                    '*'
                ),
                singleLine = true,
                keyboardActions = KeyboardActions { isPasswordError = password.isBlank() },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    val image = when {
                        passwordVisible && !isPasswordError -> Icons.Filled.Visibility
                        isPasswordError -> Icons.Filled.Error
                        else -> Icons.Filled.VisibilityOff
                    }
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                isError = isPasswordError,
                shape = RoundedCornerShape(dimensionResource(id = sdpR.dimen._6sdp).value.dp),
                placeholder = { Text(text = stringResource(id = R.string.hint_password)) },
            )

            if (isPasswordError) {
                Text(
                    text = "Password is required *",
                    color = if (isSystemInDarkTheme()) ErrorDark else Error,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Start)
                )
            }

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._8sdp).value.dp)
            )

            Text(
                text = stringResource(id = R.string.password_requirement),
                fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSystemInDarkTheme()) TertiaryDark else Tertiary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._12sdp).value.dp)
            )

            Button(
                onClick = {
                    if (name.isBlank()) {
                        isNameError = true
                        return@Button
                    }
                    if (email.isBlank()) {
                        isEmailError = true
                        return@Button
                    }
                    if (password.isBlank()) {
                        isPasswordError = true
                        return@Button
                    }

                    viewModel.createUserWithEmailAndPassword(email, password)
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isSystemInDarkTheme()) PrimaryDark else Primary),
                modifier = Modifier
                    .height(dimensionResource(id = sdpR.dimen._40sdp).value.dp)
                    .fillMaxWidth()
                    .padding(0.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.create_account),
                    fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSystemInDarkTheme()) OnPrimaryDark else OnPrimary
                )
            }

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._8sdp).value.dp)
            )

            Text(
                text = stringResource(id = R.string.or),
                fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                fontWeight = FontWeight.Normal,
                color = if (isSystemInDarkTheme()) PrimaryDark else Primary
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._8sdp).value.dp)
            )

            Button(
                onClick = {
                    navController.navigate(route = Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isSystemInDarkTheme()) Surface1Dark else Surface1),
                border = BorderStroke(
                    width = dimensionResource(id = sdpR.dimen._1sdp).value.dp,
                    color = if (isSystemInDarkTheme()) PrimaryDark else Primary,
                ),
                modifier = Modifier
                    .height(dimensionResource(id = sdpR.dimen._40sdp).value.dp)
                    .fillMaxWidth()
                    .padding(0.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSystemInDarkTheme()) PrimaryDark else Primary
                )
            }
        }
        CustomLoader(isLoading = isLoading)
        OneTapSignInWithGoogle(state = oneTapState,
            clientId = BuildConfig.CLIENT_ID,
            onTokenIdReceived = { viewModel.signInWithCredential(it) },
            onDialogDismissed = {

            })
        if (openDialog) {
            ActionDialog(
                "Success",
                "Account created successfully. We sent a verification link to ${auth.currentUser?.email}. You can ignore the link if you used Google Sign In.",
            ) {
                openDialog = it
                auth.signOut()
                navController.navigate(route = Screen.Login.route)
            }
        }
        if (message.isNotBlank()) {
            Snackbar(
                action = {
                    Button(onClick = {}) {
                        Text("Dismiss")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) { Text(text = message) }
        }
    }
}

@Preview
@Composable
fun SignupScreenPreview() {
    BabyBuyTheme {
        SignupScreen(rememberNavController())
    }
}

@Preview(name = "Signup Screen dark mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignupScreenPreviewDark() {
    BabyBuyTheme {
        SignupScreen(rememberNavController())
    }
}