package com.certified.babybuy.ui.custom_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.certified.babybuy.R
import com.certified.babybuy.ui.theme.*
import com.intuit.sdp.R as sdpR

@Composable
fun CustomLoader(
    modifier: Modifier = Modifier,
    loaderSize: Float = dimensionResource(id = sdpR.dimen._100sdp).value,
    loaderStrokeWidth: Float = dimensionResource(id = sdpR.dimen._8sdp).value,
    trackColor: Color = if (isSystemInDarkTheme()) PrimaryDark else Primary,
    text: String = "Loading...",
    textColor: Color = if (isSystemInDarkTheme()) SurfaceDark else Surface,
    textSize: Float = dimensionResource(id = sdpR.dimen._18sdp).value,
    fontFamily: FontFamily = SpaceGrotesk,
    isLoading: Boolean = false
) {

    if (isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = Black40),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .background(color = Color.Transparent),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier = Modifier.size(
                        width = loaderSize.dp,
                        height = loaderSize.dp
                    ),
                    color = trackColor,
                    strokeWidth = loaderStrokeWidth.dp
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_app_icon),
                    contentDescription = "App Icon",
                    modifier = Modifier.size(
                        width = (loaderSize * 0.6).dp,
                        height = (loaderSize * 0.6).dp
                    ),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.size(dimensionResource(id = sdpR.dimen._8sdp).value.dp))

            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily,
                fontSize = textSize.sp,
                color = textColor
            )
        }
    }
}

@Preview
@Composable
fun CustomLoaderPreview() {
    CustomLoader()
}