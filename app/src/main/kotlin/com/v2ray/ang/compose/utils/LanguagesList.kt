package com.v2ray.ang.compose.utils

import com.v2ray.ang.compose.data.LanguageModel
import com.v2ray.ang.R

fun languagesList(): List<LanguageModel> {
    return arrayListOf(
        LanguageModel("English", "English", "en", R.drawable.en),
        LanguageModel("Afrikaans", "Afrikaans", "af", R.drawable.af),
        LanguageModel("Arabic", "عربي", "ar", R.drawable.ar),
        LanguageModel("Czech", "čeština", "cs", R.drawable.cs),
        LanguageModel("Danish", "dansk", "da", R.drawable.da),
        LanguageModel("German", "Deutsch", "de", R.drawable.de),
        LanguageModel("Greek", "ελληνικά", "el", R.drawable.el),
        LanguageModel("Spanish", "español", "es", R.drawable.es),
        LanguageModel("Persian", "فارسی", "fa", R.drawable.fa),
        LanguageModel("French", "français", "fr", R.drawable.fr),
        LanguageModel("Hindi", "हिन्दी", "hi", R.drawable.hi),
        LanguageModel("Indonesian", "Bahasa Indonesia", "in", R.drawable.in_),
        LanguageModel("Italian", "italiano", "it", R.drawable.it),
        LanguageModel("Japanese", "日本語", "ja", R.drawable.ja),
        LanguageModel("Korean", "한국인", "ko", R.drawable.ko),
        LanguageModel("Malay", "məˈlā", "ms", R.drawable.ms),
        LanguageModel("Dutch", "Nederlands", "nl", R.drawable.nl),
        LanguageModel("Norwegian", "norsk", "no", R.drawable.no),
        LanguageModel("Portuguese", "Português", "pt", R.drawable.pt),
        LanguageModel("Russian", "російський", "ru", R.drawable.ru),
        LanguageModel("Thai", "ไทย", "th", R.drawable.th),
        LanguageModel("Turkish", "Türk", "tr", R.drawable.tr),
        LanguageModel("Vietnamese", "Tiếng Việt", "vi", R.drawable.vi),
        LanguageModel("Chinese", "汉语", "zh", R.drawable.zh)
    )
}