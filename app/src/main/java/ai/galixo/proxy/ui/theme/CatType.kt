package ai.galixo.proxy.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val typography = CatTypography(
    toolbarTitle = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    timerText = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    buttonText = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    itemTitle = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    regularText = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    smallText = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    extraBold = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.ExtraBold
    ),
    bold = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold
    ),
    semiBold = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.SemiBold
    ),
    medium = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Medium
    ),
    normal = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)