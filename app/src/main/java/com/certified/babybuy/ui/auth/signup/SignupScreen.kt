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
import com.certified.babybuy.R
import com.certified.babybuy.ui.custom_component.CustomOutlinedTextField
import com.certified.babybuy.ui.theme.*
import com.intuit.sdp.R as sdpR
import com.intuit.ssp.R as sspR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen() {

    var name by remember { mutableStateOf("") }
    var isNameError by rememberSaveable { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var isEmailError by rememberSaveable { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordError by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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
            onClick = { /*TODO*/ },
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
}

@Preview
@Composable
fun SignupScreenPreview() {
    BabyBuyTheme {
        SignupScreen()
    }
}

@Preview(name = "Signup Screen dark mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignupScreenPreviewDark() {
    BabyBuyTheme {
        SignupScreen()
    }
}