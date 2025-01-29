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
                FAQItem(
                    "Do I need to create an account to use Cat Proxy?",
                    "No, you don’t need an account to start using Cat Proxy. Just download the app, connect to a server, and enjoy secure and private browsing."
                ),
                FAQItem(
                    "What is split tunneling, and how does it work in Cat Proxy?",
                    "Split tunneling is a feature that allows you to control which apps use the VPN connection and which ones bypass it. With Cat Proxy, you can choose specific apps to connect directly to the internet while other apps remain protected by the VPN. This feature is useful if you want to access local services or stream content while maintaining VPN protection for other activities."
                ),
                FAQItem(
                    "What should I do if Cat Proxy isn’t connecting?",
                    "If you’re having trouble connecting, try the following steps:\n" +
                            "• Ensure your internet connection is stable.\n" +
                            "• Switch to a different server in the app.\n" +
                            "• Update the app to the latest version.\n" +
                            "• Restart your device.\n" +
                            "• If the issue persists, contact our support team at apps@galixo.ai. "
                ),
                FAQItem(
                    "Can Cat Proxy protect me on public Wi-Fi?",
                    "Yes, Cat Proxy is designed to keep you safe on public Wi-Fi networks. Public Wi-Fi hotspots are often unsecured, making your data vulnerable to hackers. Cat Proxy encrypts your internet traffic using AES-256 encryption, ensuring your sensitive information like passwords, emails, and banking details remain private and secure, even on public networks."
                ),
                FAQItem(
                    "Why should I upgrade to Cat Proxy Premium?",
                    "Upgrading to Premium gives you access to exclusive features, including unlimited connectivity time, faster servers, ad-free browsing and more! Enjoy the best VPN experience with just one tap."
                )
            )
        )
    }
}
