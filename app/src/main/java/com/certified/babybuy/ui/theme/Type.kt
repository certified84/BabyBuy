package com.certified.babybuy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.certified.babybuy.R


// Set of Material typography styles to start with
private val light = Font(R.font.space_grotesk_light, FontWeight.W300)
private val regular = Font(R.font.space_grotesk_regular, FontWeight.W400)
private val medium = Font(R.font.space_grotesk_medium, FontWeight.W500)
private val semibold = Font(R.font.space_grotesk_semi_bold, FontWeight.W600)
private val bold = Font(R.font.space_grotesk_bold, FontWeight.W700)

val SpaceGrotesk = FontFamily(light, regular, medium, semibold, bold)

val Typography = Typography(
//    body1 = TextStyle(
//        fontFamily = SpaceGrotesk,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp
//    ),
    displayLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
//    h2 = TextStyle(
//        fontFamily = SpaceGrotesk,
//        fontWeight = FontWeight.Bold,
//        fontSize = 20.sp
//    ),
//    h3 = TextStyle(
//        fontFamily = SpaceGrotesk,
//        fontWeight = FontWeight.SemiBold,
//        fontSize = 20.sp
//    ),
//    h4 = TextStyle(
//        fontFamily = SpaceGrotesk,
//        fontWeight = FontWeight.SemiBold,
//        fontSize = 18.sp
//    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)