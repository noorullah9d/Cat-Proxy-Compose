package com.galixo.proxy.compose.ui.tunneling

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.galixo.proxy.R

@Composable
fun AppIcon(
    drawable: Drawable?,
    modifier: Modifier = Modifier
) {
    // Use Coil to load the app image from a drawable
    val imagePainter = rememberAsyncImagePainter(drawable)

    Image(
        painter = imagePainter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
fun AppIconPreview() {
    val context = LocalContext.current

    // Use a mock drawable for preview
    val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.en)

    AppIcon(
        drawable = drawable,
        modifier = Modifier.size(50.dp)
    )
}
