package com.galixo.proxy.compose.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.galixo.proxy.R
import com.galixo.proxy.compose.theme.color_light_primary
import com.galixo.proxy.compose.theme.color_light_text_secondary

@Composable
fun FreeTrialFooter(
    modifier: Modifier = Modifier,
    onPrivacyPolicyClicked: () -> Unit,
    onTermsAndConditionsClicked: () -> Unit,
    clickableTextColor: Color = color_light_primary
) {
    val fullText = stringResource(id = R.string.terms_and_conditions_summary)
    val privacyPolicyText = stringResource(id = R.string.privacy_policy)
    val termsAndConditionsText = stringResource(id = R.string.terms_and_conditions)

    val annotatedText = buildAnnotatedString {
        append(fullText)

        val privacyPolicyStart = fullText.indexOf(privacyPolicyText)
        val termsAndConditionsStart = fullText.indexOf(termsAndConditionsText)

        addStyle(
            style = SpanStyle(color = clickableTextColor, fontWeight = FontWeight.Bold),
            start = privacyPolicyStart,
            end = privacyPolicyStart + privacyPolicyText.length
        )
        addStringAnnotation(
            tag = "PrivacyPolicy",
            annotation = "PrivacyPolicy",
            start = privacyPolicyStart,
            end = privacyPolicyStart + privacyPolicyText.length
        )

        addStyle(
            style = SpanStyle(color = clickableTextColor, fontWeight = FontWeight.Bold),
            start = termsAndConditionsStart,
            end = termsAndConditionsStart + termsAndConditionsText.length
        )
        addStringAnnotation(
            tag = "TermsAndConditions",
            annotation = "TermsAndConditions",
            start = termsAndConditionsStart,
            end = termsAndConditionsStart + termsAndConditionsText.length
        )
    }

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = annotatedText,
            style = MaterialTheme.typography.bodySmall.copy(color = color_light_text_secondary),
            onTextLayout = { textLayoutResult = it },
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { offset ->
                    textLayoutResult?.let { layoutResult ->
                        val position = layoutResult.getOffsetForPosition(offset)
                        annotatedText.getStringAnnotations(
                            start = position,
                            end = position
                        ).firstOrNull()?.let { annotation ->
                            when (annotation.tag) {
                                "PrivacyPolicy" -> onPrivacyPolicyClicked()
                                "TermsAndConditions" -> onTermsAndConditionsClicked()
                            }
                        }
                    }
                }
            }
        )
    }
}
