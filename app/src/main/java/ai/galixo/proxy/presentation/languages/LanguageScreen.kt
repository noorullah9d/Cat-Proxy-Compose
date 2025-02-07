package ai.galixo.proxy.presentation.languages

import ai.galixo.proxy.R
import ai.galixo.proxy.admob.admob_native_id_languages
import ai.galixo.proxy.admob.nativeAd.LoadNativeAd
import ai.galixo.proxy.admob.nativeAd.NativeAdSize
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.components.CenteredCircularProgressIndicator
import ai.galixo.proxy.presentation.components.CustomTopAppBar
import ai.galixo.proxy.presentation.components.SearchBar
import ai.galixo.proxy.presentation.main.getActivity
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.utils.Result
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.shadowsocks.preference.LanguageModel
import com.github.shadowsocks.preference.PrefUtils

@Composable
fun LanguageScreen(
    viewModel: LanguageViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val activity = getActivity()
    val languageListState = viewModel.languageListState.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredLanguages = remember(languageListState.value, searchQuery) {
        when (val state = languageListState.value) {
            is Result.Success -> {
                state.data.filter {
                    it.languageName.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true
                }
            }

            else -> emptyList()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                modifier = Modifier.background(CatTheme.colorScheme.background),
                title = stringResource(R.string.language),
                onBackPressed = {
                    viewModel.saveSelectedLanguageToPrefs(selectedLanguage)
                    navController.navigateToHomeAndFinish()
                },
                menuItems = listOf(
                    {
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    viewModel.setLanguageScreenShown(true) // will change later
                                    PrefUtils.selectedLanguage?.let { currentLanguage ->
                                        if (selectedLanguage?.languageCode != currentLanguage.languageCode) {
                                            viewModel.saveSelectedLanguageToPrefs(selectedLanguage)
                                            activity?.recreate()
                                            navController.navigateToHomeAndFinish()
                                        } else {
                                            navController.navigateToHomeAndFinish()
                                        }
                                    } ?: navController.navigateToHomeAndFinish()
                                }
                                .clip(CatTheme.shape.ad)
                                .background(CatTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null
                            )
                        }
                    }
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CatTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {
                SearchBar(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    query = searchQuery,
                    onQueryChanged = { query ->
                        searchQuery = query
                        viewModel.filterLanguages(query)
                    },
                    hint = stringResource(R.string.hint_languages),
                    shouldShowCross = false
                )
                LanguageContent(
                    modifier = Modifier.weight(1f),
                    languageListState = languageListState.value,
                    filteredLanguages = filteredLanguages,
                    selectedLanguage = selectedLanguage,
                    onUpdateLanguage = { language ->
                        viewModel.setSelectedLanguage(language)
                    }
                )
                LoadNativeAd(
                    context = context,
                    adUnitId = admob_native_id_languages,
                    adSize = NativeAdSize.LARGE
                )
            }
        }
    )
}

@Composable
fun LanguageContent(
    modifier: Modifier = Modifier,
    languageListState: Result<List<LanguageModel>>?,
    filteredLanguages: List<LanguageModel>,
    selectedLanguage: LanguageModel?,
    onUpdateLanguage: (LanguageModel) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when (languageListState) {
            is Result.Loading -> {
                CenteredCircularProgressIndicator(
                    modifier = Modifier
                        .weight(1f) // Take up remaining space
                        .fillMaxSize()
                )
            }

            is Result.Success -> {
                if (filteredLanguages.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Languages found!")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp) // Add spacing between items
                    ) {
                        items(filteredLanguages) { language ->
                            LanguageItem(
                                language = language,
                                isSelected = selectedLanguage == language,// Check if the language is selected
                                onClick = {
                                    onUpdateLanguage(language)
                                }
                            )
                        }
                    }
                }
            }

            is Result.Error -> {
                val errorMessage = languageListState.message
                Box(
                    modifier = Modifier
                        .weight(1f) // Take up remaining space
                        .fillMaxSize()
                        .background(CatTheme.colorScheme.errorSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        color = CatTheme.colorScheme.onErrorSurface,
                        style = CatTheme.typography.semiBold
                    )
                }
            }

            null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CatTheme.colorScheme.errorSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Failed to fetch languages!", color = CatTheme.colorScheme.onErrorSurface)
                }
            }
        }
    }
}

fun NavController.navigateToHomeAndFinish() {
    navigate(Destinations.HomeScreen.route) {
        popUpTo(Destinations.LanguageScreen.route) { inclusive = true }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    CatTheme {

        val dummyLanguages = listOf(
            LanguageModel(
                languageName = "English",
                languageLocalName = "English",
                languageCode = "en",
                flagDrawable = R.drawable.en,
                isSelected = true
            ),
            LanguageModel(
                languageName = "Danish",
                languageLocalName = "Danish",
                languageCode = "en",
                flagDrawable = R.drawable.da,
                isSelected = false
            ),
            LanguageModel(
                languageName = "Africans",
                languageLocalName = "Africans",
                languageCode = "en",
                flagDrawable = R.drawable.af,
                isSelected = false
            )
        )

        /*LanguageContent(
            languageListState = Result.Loading(""),
            filteredLanguages = dummyLanguages,
            selectedLanguage = null,
            onUpdateLanguage = {}
        )*/

        LanguageContent(
            languageListState = Result.Success(dummyLanguages),
            filteredLanguages = dummyLanguages,
            selectedLanguage = null,
            onUpdateLanguage = {}
        )

        /*LanguageContent(
            languageListState = Result.Error("Failed to load languages..!"),
            filteredLanguages = dummyLanguages,
            selectedLanguage = null,
            onUpdateLanguage = {}
        )*/
    }
}