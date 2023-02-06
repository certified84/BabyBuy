package com.certified.babybuy.ui.custom_component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.certified.babybuy.R
import com.certified.babybuy.ui.theme.OnSecondaryContainer
import com.certified.babybuy.ui.theme.OnSecondaryContainerDark
import com.certified.babybuy.ui.theme.SecondaryContainer
import com.certified.babybuy.ui.theme.SecondaryContainerDark

@Composable
fun CustomIconButton(icon: Int, onClick: () -> Unit, modifier: Modifier) {
    androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = modifier
            .background(
                color = if (isSystemInDarkTheme()) SecondaryContainerDark else SecondaryContainer,
                shape = CircleShape
            )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Back button",
            tint = if (isSystemInDarkTheme()) OnSecondaryContainerDark else OnSecondaryContainer,
        )
    }
}

@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier) {
    CustomIconButton(
        icon = R.drawable.ic_arrow_left,
        onClick = onClick,
        modifier = modifier
    )
}

@Preview
@Composable
fun BackButtonPreview() {
    BackButton(onClick = {}, modifier = Modifier)
}
