package ai.galixo.proxy.presentation.faqs

import ai.galixo.proxy.domain.model.FAQItem
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FAQsViewModel @Inject constructor() : ViewModel() {

    private val _faqs = mutableStateListOf<FAQItem>()
    val faqs: List<FAQItem> get() = _faqs

    init {
        _faqs.addAll(
            listOf(
                FAQItem(
                    "Is Cat Proxy safe to use?",
                    "Yes, Cat Proxy is designed with your security and privacy in mind. We use advanced encryption technology (AES-256) to ensure your data remains protected while you browse, stream, or game online. Your online activities are never logged, giving you true anonymity."
                ),
                FAQItem(
                    "Will using Cat Proxy slow down my internet?",
                    "Cat Proxy is optimized for speed with a network of high-performance servers worldwide. While any VPN may slightly impact speed due to encryption, we strive to provide ultra-fast connections for streaming, gaming, and browsing without interruptions."
                ),
                FAQItem(
                    "How do I select the best server?",
                    "Cat Proxy automatically connects you to the fastest server available. However, you can manually choose servers from different locations in the app based on your needs, such as accessing region-specific content or reducing latency for gaming."
                ),
                // Add other FAQs here...
            )
        )
    }
}
