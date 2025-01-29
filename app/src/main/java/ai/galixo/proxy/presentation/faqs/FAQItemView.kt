package ai.galixo.proxy.presentation.faqs

import ai.galixo.proxy.R
import ai.galixo.proxy.domain.model.FAQItem
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FAQItemView(
    faq: FAQItem,
    expanded: Boolean,
    onClick: () -> Unit
) {
    TopicRowSpacer(visible = expanded)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CatTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    text = faq.question,
                    style = CatTheme.typography.semiBold.copy(
                        fontSize = 15.sp
                    ),
                    color = CatTheme.colorScheme.textPrimary
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "Expand",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(if (expanded) 180f else 0f),
                    colorFilter = ColorFilter.tint(CatTheme.colorScheme.primary)
                )
            }

            // Display the answer when expanded
            if (expanded) {
                Text(
                    text = faq.answer,
                    style = CatTheme.typography.regularText,
                    color = CatTheme.colorScheme.textSecondary,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                )
            }
        }
    }
    TopicRowSpacer(visible = expanded)
}

@Composable
fun TopicRowSpacer(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun FAQItemPreview() {
    val faq = FAQItem(
        question = "Is Cat Proxy safe to use?",
        answer = "Yes, Cat Proxy is designed with your security and privacy in mind"
    )
    CatTheme {
        FAQItemView(
            faq = faq,
            expanded = true,
            onClick = {}
        )
    }
}
