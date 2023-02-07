package com.certified.babybuy.ui.auth

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.certified.babybuy.R
import com.certified.babybuy.navigation.Screen
import com.certified.babybuy.ui.theme.*
import com.intuit.sdp.R as sdpR
import com.intuit.ssp.R as sspR

@Composable
fun OnboardingScreen(navController: NavController) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(state = scrollState, orientation = Orientation.Vertical)
            .background(color = if (isSystemInDarkTheme()) PrimaryDark else Primary)
            .padding(
                top = dimensionResource(id = sdpR.dimen._24sdp).value.dp,
                start = dimensionResource(id = sdpR.dimen._16sdp).value.dp,
                end = dimensionResource(id = sdpR.dimen._16sdp).value.dp
            ), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Column {

            Image(
                painter = painterResource(id = R.drawable.ic_onboarding),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.size(dimensionResource(id = sdpR.dimen._20sdp).value.dp))

            Text(
                text = stringResource(id = R.string.onboarding_summary),
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._20ssp).value.sp,
                color = if (isSystemInDarkTheme()) OnPrimaryDark else OnPrimary
            )

        }

        Column {

            Button(
                onClick = {
                    navController.navigate(route = Screen.Signup.route) {

                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isSystemInDarkTheme()) PrimaryContainerDark else PrimaryContainer),
                modifier = Modifier
                    .height(dimensionResource(id = sdpR.dimen._40sdp).value.dp)
                    .fillMaxWidth()
                    .padding(0.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.create_account),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSystemInDarkTheme()) OnPrimaryContainerDark else OnPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.size(dimensionResource(id = sdpR.dimen._12sdp).value.dp))

            Button(
                onClick = {
                    navController.navigate(route = Screen.Login.route) {

                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface),
                modifier = Modifier
                    .height(dimensionResource(id = sdpR.dimen._40sdp).value.dp)
                    .fillMaxWidth()
                    .padding(0.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSystemInDarkTheme()) SurfaceDark else Surface
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = stringResource(id = R.string.read_about_our),
                color = if (isSystemInDarkTheme()) OnPrimaryDark else OnPrimary,
                fontSize = dimensionResource(id = sspR.dimen._13ssp).value.sp,
                modifier = Modifier.alpha(.7f)
            )

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(dimensionResource(id = sdpR.dimen._2sdp)),
            ) {
                Text(
                    text = stringResource(id = R.string.terms),
                    fontSize = dimensionResource(id = sspR.dimen._13ssp).value.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSystemInDarkTheme()) SurfaceDark else Surface
                )
            }

            Text(
                text = stringResource(id = R.string.and),
                color = if (isSystemInDarkTheme()) OnPrimaryDark else OnPrimary,
                fontSize = dimensionResource(id = sspR.dimen._13ssp).value.sp,
                modifier = Modifier.alpha(.7f)
            )

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(dimensionResource(id = sdpR.dimen._2sdp)),
            ) {
                Text(
                    text = stringResource(id = R.string.privacy_policy),
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._13ssp).value.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSystemInDarkTheme()) SurfaceDark else Surface
                )
            }
        }
    }
}

@Preview
@Composable
fun OnBoardingScreenPreview() {
    OnboardingScreen(rememberNavController())
}

@Preview(name = "Onboarding Screen Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun OnBoardingScreenPreviewDark() {
    OnboardingScreen(rememberNavController())
}
