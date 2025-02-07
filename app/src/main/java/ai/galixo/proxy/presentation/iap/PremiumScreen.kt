package ai.galixo.proxy.presentation.iap

import ai.galixo.proxy.R
import ai.galixo.proxy.presentation.components.FreeTrialFooter
import ai.galixo.proxy.presentation.components.GradientButton
import ai.galixo.proxy.presentation.main.getActivity
import ai.galixo.proxy.ui.theme.CatTheme
import ai.galixo.proxy.ui.theme.yellow
import ai.galixo.proxy.utils.DateManager.getDayOffset
import ai.galixo.proxy.utils.PremiumPlan
import ai.galixo.proxy.utils.PremiumSource
import ai.galixo.proxy.utils.extensions.isOnline
import ai.galixo.proxy.utils.extensions.toast
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(
    premiumSource: String,
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
    var prices by remember { mutableStateOf<SubscriptionPrices?>(null) }
    var source by remember { mutableStateOf<String>("") }

    PrefUtils.lastShownOfferDay = getDayOffset()
    val context = LocalContext.current
    val activity = getActivity()

    LaunchedEffect(premiumSource) {
        source = premiumSource
        Timber.d("PremiumScreen: opened from: $premiumSource")
    }

    LaunchedEffect(purchases) {
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
            val productDetails = getProductDetailsBasedOnSource(source, map)
            productDetails?.let {
                prices = getSubscriptionPrices(source, it)
                selectedPackage = it
                Timber.d("PremiumScreen: selected product= $productDetails")
                Timber.d("PremiumScreen: calculated prices= $prices")
            }
        }

        billingViewModel.acknowledgeCallback = {
            Core.stopService()
            // restart activity
            onRestart()
        }
    }

    PremiumContent(
        selectedPrices = prices,
        onWeeklyClick = {
            handleSubscriptionPurchase(context, activity, source, PremiumPlan.PREMIUM_WEEKLY, isBillingConnected, doesHaveCurrentPurchase, selectedPackage, billingViewModel)
        },
        onMonthlyClick = {
            handleSubscriptionPurchase(context, activity, source, PremiumPlan.PREMIUM_MONTHLY, isBillingConnected, doesHaveCurrentPurchase, selectedPackage, billingViewModel)
        },
        onYearlyClick = {
            handleSubscriptionPurchase(context, activity, source, PremiumPlan.PREMIUM_YEARLY, isBillingConnected, doesHaveCurrentPurchase, selectedPackage, billingViewModel)
        },
        onClose = {
            navController.popBackStack()
        },
        onPrivacyPolicyClick = { onPrivacyPolicyClick() },
        onTermsClick = { onTermsClick() }
    )
}

@Composable
fun PremiumContent(
    selectedPrices: SubscriptionPrices?,
    onClose: () -> Unit,
    onWeeklyClick: () -> Unit,
    onMonthlyClick: () -> Unit,
    onYearlyClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    Timber.d("PremiumScreen: content prices= $selectedPrices")
    val prices = selectedPrices ?: SubscriptionPrices("$0.00", "$0.00", "$0.00")
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
                painter = painterResource(R.drawable.bg_premium),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            IconButton(
                onClick = { onClose() },
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.img_premium_paw),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.container_premium_bullets2),
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
                                colorFilter = ColorFilter.tint(CatTheme.colorScheme.onPrimary)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Blazing-Fast Speed",
                                style = CatTheme.typography.medium.copy(fontSize = 17.5.sp),
                                color = CatTheme.colorScheme.onPrimary,
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
                                colorFilter = ColorFilter.tint(CatTheme.colorScheme.onPrimary)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Premium Servers Worldwide",
                                style = CatTheme.typography.medium.copy(fontSize = 17.5.sp),
                                color = CatTheme.colorScheme.onPrimary,
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
                                colorFilter = ColorFilter.tint(CatTheme.colorScheme.onPrimary)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Ad-Free Experience",
                                style = CatTheme.typography.medium.copy(fontSize = 17.5.sp),
                                color = CatTheme.colorScheme.onPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = CatTheme.colorScheme.onPrimary)) {
                            append("${stringResource(R.string.choose_your)} ")
                        }
                        withStyle(style = SpanStyle(color = yellow)) {
                            append(stringResource(R.string.premium_plan))
                        }
                    },
                    style = CatTheme.typography.toolbarTitle.copy(
                        fontSize = 24.sp
                    )
                )

                Spacer(Modifier.height(16.dp))

                SplitButton(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    leftText = stringResource(R.string.one_week),
                    leftPrice = prices.weekly,
                    rightText = stringResource(R.string.one_month),
                    rightPrice = prices.monthly,
                    onLeftClick = { onWeeklyClick() },
                    onRightClick = { onMonthlyClick() }
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.free_trial_then, prices.yearly),
                    style = CatTheme.typography.medium.copy(
                        fontSize = 14.sp
                    ),
                    color = CatTheme.colorScheme.textTertiary,
                    textAlign = TextAlign.Center
                )

                GradientButton(
                    text = stringResource(R.string.try_premium_for_free),
                    startColor = CatTheme.colorScheme.primary,
                    endColor = CatTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    onClick = {
                        onYearlyClick()
                    }
                )

                FreeTrialFooter(
                    modifier = Modifier.padding(bottom = 16.dp),
                    fullString = stringResource(id = R.string.free_trial_footer_text),
                    clickableTextColor = CatTheme.colorScheme.primary,
                    textStyle = CatTheme.typography.smallText.copy(
                        fontSize = 12.sp,
                        color = CatTheme.colorScheme.textTertiary,
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
}

private fun getProductDetailsBasedOnSource(
    source: String,
    productDetailsMap: Map<String, ProductDetails>
): ProductDetails? {
    val packageMapping = mapOf(
        PremiumSource.PREMIUM_BUTTON to BillingViewModel.PACKAGE_PREMIUM_BUTTON_CLICK,
        PremiumSource.CONNECT_BUTTON to BillingViewModel.PACKAGE_CONNECT_BUTTON_CLICK,
        PremiumSource.SERVERS to BillingViewModel.PACKAGE_PREMIUM_SERVER_CLICK
    )
    return productDetailsMap[packageMapping[source]
        ?: BillingViewModel.PACKAGE_PREMIUM_BUTTON_CLICK]
}

private fun getSubscriptionPrices(
    source: String,
    productDetails: ProductDetails
): SubscriptionPrices {
    val planMapping = mapOf(
        PremiumSource.PREMIUM_BUTTON to listOf(
            BillingViewModel.BASE_PLAN_PREMIUM_BUTTON_WEEKLY to BillingViewModel.OFFER_WEEKLY_FREE_TRIAL,
            BillingViewModel.BASE_PLAN_PREMIUM_BUTTON_MONTHLY to BillingViewModel.OFFER_MONTHLY_FREE_TRIAL,
            BillingViewModel.BASE_PLAN_PREMIUM_BUTTON_YEARLY to BillingViewModel.OFFER_YEARLY_FREE_TRIAL
        ),
        PremiumSource.CONNECT_BUTTON to listOf(
            BillingViewModel.BASE_PLAN_CONNECT_BUTTON_WEEKLY to BillingViewModel.OFFER_WEEKLY_FREE_TRIAL,
            BillingViewModel.BASE_PLAN_CONNECT_BUTTON_MONTHLY to BillingViewModel.OFFER_MONTHLY_FREE_TRIAL,
            BillingViewModel.BASE_PLAN_CONNECT_BUTTON_YEARLY to BillingViewModel.OFFER_YEARLY_FREE_TRIAL
        ),
        PremiumSource.SERVERS to listOf(
            BillingViewModel.BASE_PLAN_PREMIUM_SERVER_WEEKLY to BillingViewModel.OFFER_WEEKLY_FREE_TRIAL,
            BillingViewModel.BASE_PLAN_PREMIUM_SERVER_MONTHLY to BillingViewModel.OFFER_MONTHLY_FREE_TRIAL,
            BillingViewModel.BASE_PLAN_PREMIUM_SERVER_YEARLY to BillingViewModel.OFFER_YEARLY_FREE_TRIAL
        )
    )

    val selectedPlans = planMapping[source] ?: return SubscriptionPrices("$0.00", "$0.00", "$0.00")

    return SubscriptionPrices(
        weekly = getPriceForOffer(productDetails, selectedPlans[0].first, selectedPlans[0].second)
            ?: "$0.00",
        monthly = getPriceForOffer(productDetails, selectedPlans[1].first, selectedPlans[1].second)
            ?: "$0.00",
        yearly = getPriceForOffer(productDetails, selectedPlans[2].first, selectedPlans[2].second)
            ?: "$0.00"
    )
}

private fun getPriceForOffer(
    productDetails: ProductDetails,
    basePlanId: String,
    offerId: String?
): String? {
    productDetails.subscriptionOfferDetails?.let { offerDetailsList ->
        // Find the matching offer for the given basePlanId and optional offerId
        val matchingOffer = offerDetailsList.firstOrNull { offerDetails ->
            offerDetails.basePlanId == basePlanId && (offerId == null || offerDetails.offerId == offerId)
        }

        Timber.d("PremiumScreen: Checking BasePlan: $basePlanId, Offer: $offerId, Found: $matchingOffer")

        // Retrieve the pricing phase with a valid price
        matchingOffer?.pricingPhases?.pricingPhaseList?.let { pricingPhases ->
            val firstPricingPhase = pricingPhases.firstOrNull { it.priceAmountMicros > 0 }
            firstPricingPhase?.let {
                return "${it.priceCurrencyCode} ${(it.priceAmountMicros / 1_000_000)}"
            }
        }
    }

    Timber.d("PremiumScreen: No matching subscription offer details found for BasePlan: $basePlanId and Offer: $offerId")
    return null
}

private fun handleSubscriptionPurchase(
    context: Context,
    activity: Activity?,
    source: String,
    selectedPlan: String,
    isBillingConnected: Boolean,
    doesHaveCurrentPurchase: Boolean,
    selectedPackage: ProductDetails?,
    viewModel: BillingViewModel
) {
    if (context.isOnline()) {
        if (isBillingConnected) {
            if (!doesHaveCurrentPurchase) {
                if (selectedPackage != null) {
                    val (basePlanId, offerId) = getBasePlanAndOffer(source, selectedPlan)

                    Timber.d("PremiumScreen: selected base plan= $basePlanId")
                    Timber.d("PremiumScreen: selected offer= $offerId")
                    if (basePlanId != null && offerId != null) {
                        viewModel.buySubscription(
                            productDetails = selectedPackage,
                            basePlanId = basePlanId,
                            offerId = offerId,
                            activity = activity
                        )
                    } else { context.toast(getString(context, R.string.failed_to_connect)) }
                } else { context.toast(getString(context, R.string.failed_to_connect)) }
            } else { context.toast(getString(context, R.string.subscription_already_purchased)) }
        } else { context.toast(getString(context, R.string.failed_to_connect)) }
    } else { context.toast(getString(context, R.string.no_internet_connection)) }
}

private fun getBasePlanAndOffer(source: String?, selectedPlan: String?): Pair<String?, String?> {
    return when (source) {
        PremiumSource.PREMIUM_BUTTON -> {
            when (selectedPlan) {
                PremiumPlan.PREMIUM_WEEKLY -> BillingViewModel.BASE_PLAN_PREMIUM_BUTTON_WEEKLY to BillingViewModel.OFFER_WEEKLY_FREE_TRIAL
                PremiumPlan.PREMIUM_MONTHLY -> BillingViewModel.BASE_PLAN_PREMIUM_BUTTON_MONTHLY to BillingViewModel.OFFER_MONTHLY_FREE_TRIAL
                PremiumPlan.PREMIUM_YEARLY -> BillingViewModel.BASE_PLAN_PREMIUM_BUTTON_YEARLY to BillingViewModel.OFFER_YEARLY_FREE_TRIAL
                else -> null to null
            }
        }
        PremiumSource.CONNECT_BUTTON -> {
            when (selectedPlan) {
                PremiumPlan.PREMIUM_WEEKLY -> BillingViewModel.BASE_PLAN_CONNECT_BUTTON_WEEKLY to BillingViewModel.OFFER_WEEKLY_FREE_TRIAL
                PremiumPlan.PREMIUM_MONTHLY -> BillingViewModel.BASE_PLAN_CONNECT_BUTTON_MONTHLY to BillingViewModel.OFFER_MONTHLY_FREE_TRIAL
                PremiumPlan.PREMIUM_YEARLY -> BillingViewModel.BASE_PLAN_CONNECT_BUTTON_YEARLY to BillingViewModel.OFFER_YEARLY_FREE_TRIAL
                else -> null to null
            }
        }
        PremiumSource.SERVERS -> {
            when (selectedPlan) {
                PremiumPlan.PREMIUM_WEEKLY -> BillingViewModel.BASE_PLAN_PREMIUM_SERVER_WEEKLY to BillingViewModel.OFFER_WEEKLY_FREE_TRIAL
                PremiumPlan.PREMIUM_MONTHLY -> BillingViewModel.BASE_PLAN_PREMIUM_SERVER_MONTHLY to BillingViewModel.OFFER_MONTHLY_FREE_TRIAL
                PremiumPlan.PREMIUM_YEARLY -> BillingViewModel.BASE_PLAN_PREMIUM_SERVER_YEARLY to BillingViewModel.OFFER_YEARLY_FREE_TRIAL
                else -> null to null
            }
        }
        else -> null to null
    }
}

@Composable
@Preview(showBackground = true)
fun PremiumPreview() {
    CatTheme {
        PremiumContent(
            selectedPrices = SubscriptionPrices("$0.00", "$0.00", "$0.00"),
            onWeeklyClick = {},
            onMonthlyClick = {},
            onYearlyClick = {},
            onClose = {},
            onPrivacyPolicyClick = {},
            onTermsClick = {}
        )
    }
}