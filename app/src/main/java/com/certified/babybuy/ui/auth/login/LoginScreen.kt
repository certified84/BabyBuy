package com.certified.babybuy.ui.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.certified.babybuy.R
import com.certified.babybuy.ui.theme.*
import com.intuit.sdp.R as sdpR
import com.intuit.ssp.R as sspR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (isSystemInDarkTheme()) Surface1Dark else Surface1)
            .padding(
                top = dimensionResource(id = sdpR.dimen._24sdp).value.dp,
                start = dimensionResource(id = sdpR.dimen._16sdp).value.dp,
                end = dimensionResource(id = sdpR.dimen._16sdp).value.dp
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.login),
            fontSize = dimensionResource(id = sspR.dimen._16ssp).value.sp,
            color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
            fontWeight = FontWeight.SemiBold,
            fontFamily = SpaceGrotesk,
        )

        Spacer(
            modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._8sdp).value.dp)
        )

        Text(
            text = stringResource(id = R.string.login_summary),
            fontSize = dimensionResource(id = sspR.dimen._13ssp).value.sp,
            color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
            fontWeight = FontWeight.Normal,
            fontFamily = SpaceGrotesk,
        )

        Spacer(
            modifier = Modifier.padding(dimensionResource(id = sdpR.dimen._10sdp).value.dp)
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

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = if (isSystemInDarkTheme()) SecondaryContainerDark else SecondaryContainer),
            textStyle = TextStyle(
                color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                fontFamily = SpaceGrotesk,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
            ),
            shape = RoundedCornerShape(dimensionResource(id = sdpR.dimen._6sdp).value.dp),
            placeholder = { Text(text = stringResource(id = R.string.email_hint)) },
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
            value = password, onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = if (isSystemInDarkTheme()) SecondaryContainerDark else SecondaryContainer),
            textStyle = TextStyle(
                color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                fontFamily = SpaceGrotesk,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            shape = RoundedCornerShape(dimensionResource(id = sdpR.dimen._6sdp).value.dp),
            placeholder = { Text(text = stringResource(id = R.string.hint_password)) },
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(hint: Int) {
    var value by remember { mutableStateOf("") }
    OutlinedTextField(
        value = value, onValueChange = { value = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = hint)) },
    )
}

@Preview
@Composable
fun LoginScreenPreview() {
    BabyBuyTheme {
        LoginScreen()
    }
}