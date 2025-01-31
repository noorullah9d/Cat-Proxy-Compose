package ai.galixo.proxy.presentation.splash

import ai.galixo.proxy.R
import ai.galixo.proxy.navigation.Destinations
import ai.galixo.proxy.presentation.components.FreeTrialFooter
import ai.galixo.proxy.ui.theme.CatTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.shadowsocks.preference.PrefUtils

@Composable
fun AgreementScreen(
    navController: NavController,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CatTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // View pager goes here
                val pagerState = rememberPagerState(pageCount = { 3 })
                HorizontalPager(state = pagerState) { page ->
                    when (page) {
                        0 -> OnboardingPage(
                            title = stringResource(R.string.title_onboarding_1),
                            description = stringResource(R.string.desc_onboarding_1),
                            animRes = R.raw.anim_onboarding_1
                        )
                        1 -> OnboardingPage(
                            title = stringResource(R.string.title_onboarding_2),
                            description = stringResource(R.string.desc_onboarding_2),
                            animRes = R.raw.anim_onboarding_2
                        )
                        2 -> OnboardingPage(
                            title = stringResource(R.string.title_onboarding_3),
                            description = stringResource(R.string.desc_onboarding_3),
                            animRes = R.raw.anim_onboarding_3
                        )
                    }
                }

                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (pagerState.currentPage == iteration) CatTheme.colorScheme.primary else CatTheme.colorScheme.divider
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CatTheme.shape.ad)
                                .background(color)
                                .width(16.dp)
                                .height(4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.agreement_text),
                style = CatTheme.typography.regularText,
                color = CatTheme.colorScheme.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        PrefUtils.hasTermsAndConditionsAccepted = true
                        navController.navigate(Destinations.SplashScreen.route) {
                            popUpTo(Destinations.AgreementScreen.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CatTheme.colorScheme.primary
                    ),
                    shape = CatTheme.shape.button
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp),
                        text = stringResource(R.string.agree_and_continue),
                        style = CatTheme.typography.buttonText,
                        color = CatTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                FreeTrialFooter(
                    modifier = Modifier.padding(bottom = 16.dp),
                    fullString = stringResource(id = R.string.terms_and_conditions_summary),
                    clickableTextColor = CatTheme.colorScheme.primary,
                    textStyle = CatTheme.typography.smallText.copy(
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
}

@Preview(showBackground = true)
@Composable
fun AgreementPreview() {
    CatTheme {
        AgreementScreen(
            navController = rememberNavController(),
            onPrivacyPolicyClick = {},
            onTermsClick = {}
        )
    }
}