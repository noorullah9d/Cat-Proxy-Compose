package ai.galixo.proxy.presentation.feedback

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.CustomTopAppBar
import ai.galixo.proxy.presentation.dialogs.FeedbackSuccessDialog
import ai.galixo.proxy.presentation.dialogs.LoadingDialog
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.extensions.toast
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val (isDialogVisible, setDialogVisible) = remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                modifier = Modifier,
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
                    viewModel.sendFeedback(feedback,
                        callback = {
                            if (it) {
                                setDialogVisible(false)
                                showSuccessDialog = true
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

    if (showSuccessDialog) {
        FeedbackSuccessDialog(
            onBackToHome = {
                showSuccessDialog = false
                onBackPressed()
            }
        )
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
            .background(CatTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.feedback_title),
            style = CatTheme.typography.bold.copy(
                fontSize = 16.sp
            ),
            color = CatTheme.colorScheme.textPrimary
        )
        /*Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tell us what can be improved?",
            style = MaterialTheme.typography.bodySmall,
            color = CatTheme.colorScheme.textPrimary
        )*/
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
                label = {
                    Text(
                        text = category,
                        style = CatTheme.typography.normal,
                        color = CatTheme.colorScheme.textPrimary
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CatTheme.colorScheme.primary,
                    selectedLabelColor = CatTheme.colorScheme.onPrimary,
                    containerColor = CatTheme.colorScheme.surface,
                    labelColor = CatTheme.colorScheme.onSurface
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
            .clip(CatTheme.shape.tile) // Increased corner radius
            .border(
                width = 1.dp,
                color = CatTheme.colorScheme.primary, // Stroke color
                shape = CatTheme.shape.tile
            ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = CatTheme.colorScheme.onPrimary
        ),
        placeholder = {
            Text(
                text = "Tell us what can we improve...",
                style = CatTheme.typography.medium,
                color = CatTheme.colorScheme.textSecondary
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
                Toast.makeText(context, context.getString(R.string.no_feedback), Toast.LENGTH_SHORT).show()
            } else {
                val finalFeedback = buildFeedbackString(selectedCategories, feedback)
                onSubmit(finalFeedback)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = CatTheme.colorScheme.primary
        ),
        shape = CatTheme.shape.button
    ) {
        Text(
            modifier = Modifier.padding(vertical = 6.dp),
            text = "Submit",
            style = CatTheme.typography.bold.copy(
                fontSize = 16.sp
            ),
            color = CatTheme.colorScheme.onPrimary
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
    CatTheme {
        val context = LocalContext.current
        FeedbackContent(
            context = context
        ) {}
    }
}