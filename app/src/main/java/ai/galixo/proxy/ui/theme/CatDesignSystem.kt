package ai.galixo.proxy.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

// colors
data class CatColorScheme(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val adBackground: Color,
    val shimmer: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val divider: Color,
    val errorSurface: Color,
    val onErrorSurface: Color
)

// typography
data class CatTypography(
    val toolbarTitle: TextStyle,
    val buttonText: TextStyle,
    val itemTitle: TextStyle,
    val regularText: TextStyle,
    val smallText: TextStyle,
    val extraBold: TextStyle,
    val bold: TextStyle,
    val semiBold: TextStyle,
    val medium: TextStyle,
    val normal: TextStyle
)

// shapes
data class CatShape(
    val dialog: Shape,
    val tile: Shape,
    val button: Shape,
    val ad: Shape
)

// sizes
data class CatSize(
    val large: Dp,
    val medium: Dp,
    val normal: Dp,
    val small: Dp
)

val LocalCatColorScheme = staticCompositionLocalOf {
    CatColorScheme(
        primary = Color.Unspecified,
        secondary = Color.Unspecified,
        background = Color.Unspecified,
        surface = Color.Unspecified,
        onPrimary = Color.Unspecified,
        onSecondary = Color.Unspecified,
        onBackground = Color.Unspecified,
        onSurface = Color.Unspecified,
        adBackground = Color.Unspecified,
        shimmer = Color.Unspecified,
        textPrimary = Color.Unspecified,
        textSecondary = Color.Unspecified,
        textTertiary = Color.Unspecified,
        divider = Color.Unspecified,
        errorSurface = Color.Unspecified,
        onErrorSurface = Color.Unspecified
    )
}

val LocalCatTypography = staticCompositionLocalOf {
    CatTypography(
        toolbarTitle = TextStyle.Default,
        buttonText = TextStyle.Default,
        itemTitle = TextStyle.Default,
        regularText = TextStyle.Default,
        smallText = TextStyle.Default,
        extraBold = TextStyle.Default,
        bold = TextStyle.Default,
        semiBold = TextStyle.Default,
        medium = TextStyle.Default,
        normal = TextStyle.Default
    )
}

val LocalCatShape = staticCompositionLocalOf {
    CatShape(
        dialog = RectangleShape,
        tile = RectangleShape,
        button = RectangleShape,
        ad = RectangleShape
    )
}

val LocalCatSize = staticCompositionLocalOf {
    CatSize(
        large = Dp.Unspecified,
        medium = Dp.Unspecified,
        normal = Dp.Unspecified,
        small = Dp.Unspecified
    )
}