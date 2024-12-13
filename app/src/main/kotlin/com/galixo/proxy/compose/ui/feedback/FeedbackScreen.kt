package com.galixo.proxy.compose.ui.feedback

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.galixo.proxy.R
import com.galixo.proxy.compose.theme.CatProxyTheme
import com.galixo.proxy.compose.theme.color_light_background
import com.galixo.proxy.compose.theme.color_light_btn_text
import com.galixo.proxy.compose.theme.color_light_primary
import com.galixo.proxy.compose.theme.color_light_text_primary
import com.galixo.proxy.compose.theme.color_light_text_secondary
import com.galixo.proxy.compose.theme.color_light_tile
import com.galixo.proxy.compose.ui.components.CustomTopAppBar
import com.galixo.proxy.compose.ui.components.LoadingDialog
import com.galixo.proxy.extension.toast

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val (isDialogVisible, setDialogVisible) = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.feedback),
                onBackPressed = onBackPressed,
                menuItems = listOf()
            )
        },
        content = { paddingValues ->
            FeedbackContent(
                context = context,
                modifier = Modifier.padding(paddingValues),
                onFeedbackSubmit = { feedback ->
                    setDialogVisible(true)
                    context.toast("Submitting feedback...")
                    viewModel.sendFeedback(feedback,
                        callback = {
                            if (it) {
                                context.toast("Feedback submitted: $feedback")
                                setDialogVisible(false)
                                /*
                                activity?.showFeedbackSuccessDialog {
                                    findNavController().popBackStack()
                                }*/
                            } else {
                                setDialogVisible(false)
                                val failedMessage = ContextCompat.getString(
                                    context,
                                    R.string.failed_to_send_feedback
                                )
                                context.toast(failedMessage)
                            }
                        }
                    )
                }
            )
        }
    )

    if (isDialogVisible) {
        LoadingDialog(onDismissRequest = {})
    }
}

@Composable
fun FeedbackContent(
    context: Context,
    modifier: Modifier = Modifier,
    onFeedbackSubmit: (String) -> Unit
) {
    var selectedCategories by remember { mutableStateOf<List<String>>(emptyList()) }
    var feedbackText by remember { mutableStateOf("") }

    val categories = listOf(
        "Overall Service",
        "In-app Purchases",
        "Speed & Efficiency",
        "Location Missing",
        "Connectivity Issue",
        "App Crashes"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color_light_background)
            .padding(16.dp)
    ) {
        Text(
            text = "Rate Your Experience",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Normal,
                color = color_light_text_primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tell us what can be improved?",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                color = color_light_text_primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        FeedbackChips(
            categories = categories,
            selectedCategories = selectedCategories,
            onCategorySelected = { category ->
                selectedCategories = selectedCategories + category
            },
            onCategoryDeselected = { category ->
                selectedCategories = selectedCategories - category
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        FeedbackInputField(
            value = feedbackText,
            onValueChange = { feedbackText = it }
        )
        Spacer(modifier = Modifier.height(24.dp))
        SubmitButton(
            context = context,
            selectedCategories = selectedCategories,
            feedback = feedbackText,
            onSubmit = onFeedbackSubmit
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedbackChips(
    categories: List<String>,
    selectedCategories: List<String>,
    onCategorySelected: (String) -> Unit,
    onCategoryDeselected: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Horizontal spacing between chips
    ) {
        categories.forEach { category ->
            val isSelected = selectedCategories.contains(category)

            FilterChip(
                selected = isSelected,
                onClick = {
                    if (isSelected) {
                        onCategoryDeselected(category)
                    } else {
                        onCategorySelected(category)
                    }
                },
                shape = RoundedCornerShape(100.dp),
                label = { Text(text = category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color_light_primary,
                    selectedLabelColor = color_light_background,
                    containerColor = color_light_tile,
                    labelColor = color_light_text_secondary
                )
            )
        }
    }
}

@Composable
fun FeedbackInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp) // Minimum height for the input field
            .clip(RoundedCornerShape(16.dp)) // Increased corner radius
            .border(
                width = 1.dp,
                color = color_light_text_secondary, // Stroke color
                shape = RoundedCornerShape(16.dp)
            ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = color_light_primary
        ),
        placeholder = {
            Text(
                text = "Tell us what can we improve...",
                color = color_light_text_secondary
            )
        },
        maxLines = 5 // Restrict maximum lines to 5
    )
}

@Composable
fun SubmitButton(
    context: Context,
    selectedCategories: List<String>,
    feedback: String,
    onSubmit: (String) -> Unit
) {
    Button(
        onClick = {
            if (selectedCategories.isEmpty() && feedback.isBlank()) {
                Toast.makeText(context, "Describe at least one issue", Toast.LENGTH_SHORT).show()
            } else {
                val finalFeedback = buildFeedbackString(selectedCategories, feedback)
                onSubmit(finalFeedback)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = color_light_primary // Background color
        ),
        shape = RoundedCornerShape(100.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = "Submit",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                color = color_light_btn_text
            )
        )
    }
}

fun buildFeedbackString(selectedChips: List<String>, feedbackInput: String): String {
    val issues = selectedChips.joinToString(", ")
    return if (issues.isNotBlank()) {
        "$issues. $feedbackInput"
    } else {
        feedbackInput
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackPreview() {
    CatProxyTheme {
        val context = LocalContext.current
        FeedbackContent(
            context = context
        ) {}
    }
}