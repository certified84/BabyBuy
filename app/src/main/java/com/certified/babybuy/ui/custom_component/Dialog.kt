package com.certified.babybuy.ui.custom_component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.certified.babybuy.ui.theme.SpaceGrotesk
import com.intuit.ssp.R as sspR

@Composable
fun ActionDialog(title: String, message: String, action: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { action(false) },
        title = {
            Text(
                text = title,
                fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = message,
                fontSize = dimensionResource(id = sspR.dimen._12ssp).value.sp,
                fontFamily = SpaceGrotesk
            )
        },
        confirmButton = {
            Button(onClick = { action(false) }) {
                Text(
                    text = "OK",
                    fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                    fontFamily = SpaceGrotesk
                )
            }
        },
        dismissButton = null
    )
}

@Composable
fun YesNoDialog(
    title: String, message: String, action: ((Boolean) -> Unit)? = null,
    cancelAction: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = { cancelAction?.invoke() },
        title = {
            Text(
                text = title,
                fontSize = dimensionResource(id = sspR.dimen._14ssp).value.sp,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = message,
                fontSize = dimensionResource(id = sspR.dimen._12ssp).value.sp,
                fontFamily = SpaceGrotesk
            )
        },
        confirmButton = {
            Button(onClick = { action?.invoke(false) }) {
                Text(
                    text = "Yes",
                    fontSize = dimensionResource(id = sspR.dimen._13ssp).value.sp,
                    fontFamily = SpaceGrotesk
                )
            }
        },
        dismissButton = {
            Button(onClick = { cancelAction?.invoke() }) {
                Text(
                    text = "No",
                    fontSize = dimensionResource(id = sspR.dimen._13ssp).value.sp,
                    fontFamily = SpaceGrotesk
                )
            }
        }
    )
}