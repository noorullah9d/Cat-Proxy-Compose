package ai.galixo.proxy.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import timber.log.Timber

private val darkColorScheme = CatColorScheme(
    primary = dark_primary,
    secondary = dark_secondary,
    background = dark_background,
    surface = dark_surface,
    onPrimary = dark_on_primary,
    onSecondary = dark_on_secondary,
    onBackground = dark_on_background,
    onSurface = dark_on_surface,
    adBackground = dark_ad_background,
    shimmer = dark_shimmer,
    textPrimary = dark_text_primary,
    textSecondary = dark_text_secondary,
    textTertiary = dark_text_tertiary,
    divider = dark_divider,
    errorSurface = dark_error_container,
    onErrorSurface = dark_on_error_container
)

private val lightColorScheme = CatColorScheme(
    primary = light_primary,
    secondary = light_secondary,
    background = light_background,
    surface = light_surface,
    onPrimary = light_on_primary,
    onSecondary = light_on_secondary,
    onBackground = light_on_background,
    onSurface = light_on_surface,
    adBackground = light_ad_background,
    shimmer = light_shimmer,
    textPrimary = light_text_primary,
    textSecondary = light_text_secondary,
    textTertiary = light_text_tertiary,
    divider = light_divider,
    errorSurface = light_error_container,
    onErrorSurface = light_on_error_container
)

@Composable
fun CatTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    navController: NavController = NavController(LocalContext.current),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isDarkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    // Get the current destination
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    Timber.d("Theme: current des= $currentDestination")

    // Add primary status bar color from chosen color scheme.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val statusBarColor = when (currentDestination) {
                "splash" -> colorScheme.primary
                "free_trial" -> indigo
                else -> colorScheme.background
            }
            // Set status bar color
            window.statusBarColor = statusBarColor.toArgb()
            // Determine if light icons should be used based on color brightness
            val isLightStatusBar = statusBarColor.luminance() > 0.5f
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isLightStatusBar
        }
    }

    CompositionLocalProvider(
        LocalCatColorScheme provides colorScheme,
        LocalCatTypography provides typography,
        LocalCatShape provides shape,
        LocalCatSize provides size,
        content = content
    )
}

object CatTheme {
    val colorScheme: CatColorScheme
        @Composable get() = LocalCatColorScheme.current

    val typography: CatTypography
        @Composable get() = LocalCatTypography.current

    val shape: CatShape
        @Composable get() = LocalCatShape.current

    val size: CatSize
        @Composable get() = LocalCatSize.current
}