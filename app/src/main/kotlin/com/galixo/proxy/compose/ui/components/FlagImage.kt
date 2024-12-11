package com.galixo.proxy.compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.galixo.proxy.R

@Composable
fun FlagImage(
    flagCode: String?,
    modifier: Modifier = Modifier
) {
    // Use Coil to load the flag image from a URL
    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://cdn.jsdelivr.net/gh/hjnilsson/country-flags@master/png250px/$flagCode.png")
            .crossfade(true)
            .placeholder(R.drawable.en) // Placeholder image while loading
//            .error(R.drawable.ic_globe_alt) // Error image if loading fails
            .build()
    )

    // Load the image using Image composable from Coil
    Image(
        painter = imagePainter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
fun FlagImagePreview() {
    FlagImage(
        flagCode = "pk",
        modifier = Modifier.size(50.dp)
    )
}
