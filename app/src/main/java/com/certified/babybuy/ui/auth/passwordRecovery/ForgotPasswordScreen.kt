package com.certified.babybuy.ui.auth.passwordRecovery

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.certified.babybuy.R
import com.certified.babybuy.navigation.Screen
import com.certified.babybuy.ui.custom_component.BackButton
import com.certified.babybuy.ui.custom_component.CustomLoader
import com.certified.babybuy.ui.custom_component.CustomOutlinedTextField
import com.certified.babybuy.ui.theme.*
import com.certified.babybuy.util.UIState
import com.intuit.sdp.R as sdpR
import com.intuit.ssp.R as sspR

@Composable
fun ForgotPasswordScreen(navController: NavController) {

    val viewModel = hiltViewModel<ForgotPasswordViewModel>()

    var email by remember { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    var isLoading by rememberSaveable { mutableStateOf(false) }
    viewModel.uiState.collectAsState().value.let {
        isLoading = it == UIState.LOADING
    }

    var success by rememberSaveable { mutableStateOf(false) }
    viewModel.success.collectAsState().value.let {
        success = it
    }

    if (success) {
        LaunchedEffect(true) {
            viewModel._success.value = false
        }
        navController.navigate(route = Screen.Login.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(
                    state = scrollState, orientation = Orientation.Vertical
                )
                .background(color = if (isSystemInDarkTheme()) Surface1Dark else Surface1)
                .padding(
                    top = dimensionResource(id = sdpR.dimen._24sdp).value.dp,
                    start = dimensionResource(id = sdpR.dimen._16sdp).value.dp,
                    end = dimensionResource(id = sdpR.dimen._16sdp).value.dp
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BackButton(
                    onClick = {
                        navController.navigate(route = Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }, modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.size(dimensionResource(id = sdpR.dimen._10sdp).value.dp))

                Text(
                    text = stringResource(id = R.string.password_recovery),
                    fontSize = dimensionResource(id = sspR.dimen._16ssp).value.sp,
                    color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = SpaceGrotesk,
                    onTextLayout = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                )

            }

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._8sdp).value.dp)
            )

            Text(
                text = stringResource(id = R.string.password_recovery_summary),
                fontSize = dimensionResource(id = sspR.dimen._13ssp).value.sp,
                color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                fontWeight = FontWeight.Normal,
                fontFamily = SpaceGrotesk,
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._12sdp).value.dp)
            )

            Text(
                text = stringResource(id = R.string.email_address),
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
                    isError = it.isBlank()
                },
                placeholder = stringResource(id = R.string.email_hint),
                errorText = "Email is required *",
                isError = isError,
                keyboardActions = KeyboardActions { isError = email.isBlank() },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
                )
            )

            Spacer(
                modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._12sdp).value.dp)
            )

            Button(
                onClick = {
                    if (email.isBlank()) {
                        isError = true
                        return@Button
                    }
                    viewModel.sendPasswordResetEmail(email)
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isSystemInDarkTheme()) PrimaryDark else Primary),
                modifier = Modifier
                    .height(dimensionResource(id = sdpR.dimen._40sdp).value.dp)
                    .fillMaxWidth()
                    .padding(0.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.reset_password),
                    fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSystemInDarkTheme()) OnPrimaryDark else OnPrimary
                )
            }
        }
        CustomLoader(isLoading = isLoading)
    }
}

@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    BabyBuyTheme {
        ForgotPasswordScreen(rememberNavController())
    }
}

@Preview(name = "Login Screen dark mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ForgotPasswordScreenPreviewDark() {
    BabyBuyTheme {
        ForgotPasswordScreen(rememberNavController())
    }
}