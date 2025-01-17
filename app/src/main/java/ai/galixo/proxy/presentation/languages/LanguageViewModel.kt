package ai.galixo.proxy.presentation.languages

import ai.galixo.proxy.domain.repository.LanguageRepo
import ai.galixo.proxy.utils.Result
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.shadowsocks.preference.LanguageModel
import com.github.shadowsocks.preference.PrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val selectLanguageRepo: LanguageRepo
) : ViewModel() {

    private val _languageListState = MutableStateFlow<Result<List<LanguageModel>>?>(null)
    val languageListState: StateFlow<Result<List<LanguageModel>>?> = _languageListState

    private val _selectedLanguage = MutableStateFlow<LanguageModel?>(null)
    val selectedLanguage: StateFlow<LanguageModel?> = _selectedLanguage

    private var allLanguages: ArrayList<LanguageModel> = arrayListOf()

    init {
        getLanguagesList()
        loadSelectedLanguageFromPrefs()
    }

    private fun getLanguagesList() = viewModelScope.launch {
            selectLanguageRepo.getLanguageList().collect { result ->
                if (result is Result.Success) {
                    allLanguages = result.data as ArrayList
                }
                _languageListState.value = result
            }
        }

    fun filterLanguages(query: String) {
        val filteredApps = if (query.isEmpty()) {
            allLanguages
        } else {
            allLanguages.filter {
                it.languageName.contains(query, ignoreCase = true) == true
            } as ArrayList<LanguageModel>
        }
        _languageListState.value = Result.Success(filteredApps)
    }

    fun setSelectedLanguage(language: LanguageModel) {
        _selectedLanguage.value = language
    }

    fun setLanguageScreenShown(isLanguageShown: Boolean) {
        PrefUtils.isLanguagesShown = isLanguageShown
    }

    fun saveSelectedLanguageToPrefs(language: LanguageModel?) {
        PrefUtils.selectedLanguage = language
    }

    private fun loadSelectedLanguageFromPrefs() {
        val language = PrefUtils.selectedLanguage
        if (language != null) {
            _selectedLanguage.value = language
        } else {
            val defaultLanguage = selectLanguageRepo.getDefaultLanguage()
            _selectedLanguage.value = defaultLanguage
            saveSelectedLanguageToPrefs(defaultLanguage)
        }
    }
}