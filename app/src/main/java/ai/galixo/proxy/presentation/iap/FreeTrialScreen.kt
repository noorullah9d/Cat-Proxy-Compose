package ai.galixo.proxy.presentation.iap

import ai.galixo.proxy.R
import ai.galixo.proxy.admob.freeTrialKey
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.components.FreeTrialFooter
import ai.galixo.proxy.presentation.components.GradientButton
import ai.galixo.proxy.presentation.main.getActivity
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.ui.theme.lightOrange
import ai.galixo.proxy.ui.theme.orange
import ai.galixo.proxy.utils.DateManager.getDayOffset
import ai.galixo.proxy.utils.extensions.isOnline
import ai.galixo.proxy.utils.extensions.toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.android.billingclient.api.ProductDetails
import com.github.shadowsocks.Core
import com.github.shadowsocks.preference.PrefUtils
import timber.log.Timber

@Composable
fun FreeTrialScreen(
    billingViewModel: BillingViewModel,
    navController: NavController,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onRestart: () -> Unit
) {

    val hasActiveSubs by billingViewModel.hasActiveSubs.observeAsState(false)
    val purchases by billingViewModel.purchases.collectAsStateWithLifecycle(initialValue = null)
    val isBillingConnected by billingViewModel.isBillingConnected().observeAsState(initial = true)

    var selectedPackage by remember { mutableStateOf<ProductDetails?>(null) }
    var doesHaveCurrentPurchase by remember { mutableStateOf(false) }
    var selectedPrice by remember { mutableStateOf<String?>("") }

    PrefUtils.lastShownOfferDay = getDayOffset()
    val context = LocalContext.current
    val activity = getActivity()

    LaunchedEffect(purchases) {
        Timber.d("IAP: purchaseList = $purchases")
        PrefUtils.isAdsRemoved = purchases?.isNotEmpty() == true
    }

    LaunchedEffect(hasActiveSubs) {
        doesHaveCurrentPurchase = hasActiveSubs
    }

    LaunchedEffect(isBillingConnected) {
        if (!isBillingConnected) {
            context.toast(getString(context, R.string.failed_to_connect_to_billing_server))
        }
    }

    LaunchedEffect(Unit) {
        billingViewModel.getProductDetails().observeForever { map ->
            val productDetails = getProductDetailsBasedOnSource(map)
            productDetails?.let {
                selectedPrice = assignPackages(it)
                selectedPackage = it
                Timber.d("IAP: offer details = $it --- --- \nprice= $selectedPrice")
            }
        }

        billingViewModel.acknowledgeCallback = {
            Core.stopService()
            // restart activity
            onRestart()
        }
    }

    FreeTrialContent(
        selectedPrice = selectedPrice,
        onPurchaseClick = {
            if (context.isOnline()) {
                if (billingViewModel.isBillingConnected().value == true) {
                    if (!doesHaveCurrentPurchase) {
                        if (selectedPackage != null) {
                            val dayOffset = getDayOffset()
                            val (basePlanId, offerId) = when (dayOffset) {
                                1 -> when (freeTrialKey) {
                                    0 -> BillingViewModel.BASE_PLAN_SPLASH_YEARLY to BillingViewModel.OFFER_3DAY_FREE_TRIAL
                                    1 -> BillingViewModel.BASE_PLAN_SPLASH_YEARLY to BillingViewModel.OFFER_7DAY_FREE_TRIAL
                                    else -> BillingViewModel.BASE_PLAN_SPLASH_YEARLY to null
                                }

                                2 -> BillingViewModel.BASE_PLAN_RETURNING_SPLASH_YEARLY to BillingViewModel.OFFER_25_FREE_TRIAL
                                3 -> BillingViewModel.BASE_PLAN_RETURNING_SPLASH_YEARLY to BillingViewModel.OFFER_35_FREE_TRIAL
                                4 -> BillingViewModel.BASE_PLAN_RETURNING_SPLASH_YEARLY to BillingViewModel.OFFER_50_FREE_TRIAL
                                else -> null to null
                            }

                            Timber.d("IAP: onPurchaseClick: basePlan=$basePlanId -- offerId=$offerId")
                            if (basePlanId != null) {
                                billingViewModel.buySubscription(
                                    productDetails = selectedPackage!!,
                                    basePlanId = basePlanId,
                                    offerId = offerId,
                                    activity = activity
                                )
                            } else {
                                context.toast(getString(context, R.string.failed_to_connect))
                            }
                        } else {
                            context.toast(getString(context, R.string.failed_to_connect))
                        }
                    } else {
                        context.toast(getString(context, R.string.subscription_already_purchased))
                    }
                } else {
                    context.toast(getString(context, R.string.failed_to_connect))
                }
            } else {
                context.toast(getString(context, R.string.no_internet_available))
            }
        },
        navigateForward = {
            navController.navigateForward(billingViewModel)
        },
        onPrivacyPolicyClick = { onPrivacyPolicyClick() },
        onTermsClick = { onTermsClick() }
    )
}

@Composable
fun FreeTrialContent(
    selectedPrice: String?,
    navigateForward: () -> Unit,
    onPurchaseClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    val price = selectedPrice ?: "$29.99"
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CatTheme.colorScheme.background)
        ) {
            Image(
                painter = painterResource(R.drawable.img_free_trial_header),
                contentDescription = null,
                contentScale = ContentScale.Crop,  // Ensures the image covers the width
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            IconButton(
                onClick = { navigateForward() },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    modifier = Modifier.size(34.dp),
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = CatTheme.colorScheme.onPrimary
                )
            }
        }

        // Main Content below the image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.container_premium_bullets),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 52.dp, end = 32.dp)
                        .align(Alignment.Center)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_rocket),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            colorFilter = ColorFilter.tint(CatTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Blazing-Fast Speeds",
                            style = CatTheme.typography.semiBold.copy(fontSize = 17.5.sp),
                            color = CatTheme.colorScheme.textSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_crown_empty),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            colorFilter = ColorFilter.tint(CatTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Premium Servers Worldwide",
                            style = CatTheme.typography.semiBold.copy(fontSize = 17.5.sp),
                            color = CatTheme.colorScheme.textSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_uniterrupted),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            colorFilter = ColorFilter.tint(CatTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Uninterrupted Connection",
                            style = CatTheme.typography.semiBold.copy(fontSize = 17.5.sp),
                            color = CatTheme.colorScheme.textSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_no_ads),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            colorFilter = ColorFilter.tint(CatTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Ad-Free Experience",
                            style = CatTheme.typography.semiBold.copy(fontSize = 17.5.sp),
                            color = CatTheme.colorScheme.textSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            GradientButton(
                text = stringResource(R.string.try_premium_for_free),
                startColor = orange,
                endColor = lightOrange,
                modifier = Modifier.padding(16.dp),
                onClick = {
                    onPurchaseClick()
                }
            )

            Text(
                text = stringResource(R.string.free_trial_then, price),
                style = CatTheme.typography.regularText.copy(
                    fontSize = 14.sp
                ),
                color = CatTheme.colorScheme.textSecondary,
                textAlign = TextAlign.Center
            )

            FreeTrialFooter(
                modifier = Modifier.padding(vertical = 16.dp),
                fullString = stringResource(id = R.string.free_trial_footer_text),
                clickableTextColor = CatTheme.colorScheme.primary,
                textStyle = CatTheme.typography.smallText.copy(
                    fontSize = 12.sp,
                    color = CatTheme.colorScheme.textSecondary,
                    textAlign = TextAlign.Center
                ),
                onPrivacyPolicyClicked = {
                    onPrivacyPolicyClick()
                },
                onTermsAndConditionsClicked = {
                    onTermsClick()
                }
            )
        }
    }
}

private fun getProductDetailsBasedOnSource(productDetailsMap: Map<String, ProductDetails>): ProductDetails? {
    return when (getDayOffset()) {
        1 -> {
            productDetailsMap[BillingViewModel.PACKAGE_SPLASH_YEARLY]
        }

        else -> productDetailsMap[BillingViewModel.PACKAGE_RETURNING_SPLASH_YEARLY]
    }
}

private fun assignPackages(productDetails: ProductDetails): String {
    val defaultPrices = mapOf(
        1 to "$29.99",
        2 to "$21.99",
        3 to "$19.99",
        4 to "$14.99"
    )

    val (basePlanId, offerId, fallbackPrice) = when (getDayOffset()) {
        1 -> Triple(
            BillingViewModel.BASE_PLAN_SPLASH_YEARLY,
            when (freeTrialKey) {
                0 -> BillingViewModel.OFFER_3DAY_FREE_TRIAL
                1 -> BillingViewModel.OFFER_7DAY_FREE_TRIAL
                else -> null
            },
            defaultPrices[1]
        )
        2 -> Triple(
            BillingViewModel.BASE_PLAN_RETURNING_SPLASH_YEARLY,
            BillingViewModel.OFFER_25_FREE_TRIAL,
            defaultPrices[2]
        )
        3 -> Triple(
            BillingViewModel.BASE_PLAN_RETURNING_SPLASH_YEARLY,
            BillingViewModel.OFFER_35_FREE_TRIAL,
            defaultPrices[3]
        )
        4 -> Triple(
            BillingViewModel.BASE_PLAN_RETURNING_SPLASH_YEARLY,
            BillingViewModel.OFFER_50_FREE_TRIAL,
            defaultPrices[4]
        )
        else -> Triple(null, null, "$29.99")
    }

    val price = getPriceForOffer(productDetails, basePlanId, offerId)
    Timber.d("IAP: selected offer = $price")

    return price ?: fallbackPrice!!
}


private fun getPriceForOffer(
    productDetails: ProductDetails,
    basePlanId: String?,
    offerId: String? = null
): String? {
    productDetails.subscriptionOfferDetails?.let { offerDetailsList ->
        // Find the matching offer for the given basePlanId and offerId
        val matchingOffer = offerDetailsList.firstOrNull { offerDetails ->
            offerDetails.basePlanId == basePlanId && (offerId == null || offerDetails.offerId == offerId)
        }

        // Retrieve the pricing phase with a valid price
        matchingOffer?.pricingPhases?.pricingPhaseList?.let { pricingPhases ->
            val firstPricingPhase = pricingPhases.firstOrNull { it.priceAmountMicros > 0 }
            if (firstPricingPhase != null) {
                return "${firstPricingPhase.priceCurrencyCode} ${(firstPricingPhase.priceAmountMicros / 1_000_000)}"
            }
        }
    }
    // Return null if no price is found
    return null
}

private fun NavController.navigateForward(billingViewModel: BillingViewModel) {
    if (!billingViewModel.isLanguageShown() && getDayOffset() == 1) {
        // navigate to language
        navigate(Destinations.LanguageScreen.route) {
            popUpTo(Destinations.FreeTrialScreen.route) { inclusive = true }
        }
    } else {
        navigate(Destinations.HomeScreen.route) {
            popUpTo(Destinations.FreeTrialScreen.route) { inclusive = true }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FreeTrialPreview() {
    CatTheme {
        FreeTrialContent(
            selectedPrice = "$29.99",
            onPurchaseClick = {},
            navigateForward = {},
            onPrivacyPolicyClick = {},
            onTermsClick = {}
        )
    }
}