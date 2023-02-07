package com.certified.babybuy.ui.custom_component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.certified.babybuy.ui.theme.*
import com.intuit.sdp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "",
    errorText: String = "",
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {

    Column(modifier = modifier
        .fillMaxWidth()
        .background(color = Color.Transparent)) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = if (isSystemInDarkTheme()) SecondaryContainerDark else SecondaryContainer),
            textStyle = TextStyle(
                color = if (isSystemInDarkTheme()) OnSurfaceDark else OnSurface,
                fontFamily = SpaceGrotesk,
            ),
            isError = isError,
            trailingIcon = {
                if (isError) Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    tint = if (isSystemInDarkTheme()) ErrorDark else Error
                )
            },
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen._6sdp).value.dp),
            placeholder = { Text(text = placeholder) },
        )

        if (isError) {
            Text(
                text = errorText,
                color = if (isSystemInDarkTheme()) ErrorDark else Error,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.Start)
            )
        }
    }
}