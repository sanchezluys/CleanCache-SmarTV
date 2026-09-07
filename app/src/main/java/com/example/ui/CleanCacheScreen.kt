package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.example.ui.components.CleanActionButton
import com.example.ui.components.CleanResultModal
import com.example.ui.components.MetricGaugesRow
import com.example.ui.components.TechnicalSpecsCard
import com.example.ui.components.TvFooter
import com.example.ui.components.TvHeader
import com.example.ui.theme.TvBackgroundDark

@Composable
fun CleanCacheScreen(
    uiState: MainUiState,
    onCleanClicked: () -> Unit,
    onDismissResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonFocusRequester = remember { FocusRequester() }

    // Request initial focus on the main action button for TV remote control ergonomics
    LaunchedEffect(Unit) {
        try {
            buttonFocusRequester.requestFocus()
        } catch (_: Exception) {
            // Ignore if layout not yet bound
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TvBackgroundDark)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            val isWide = maxWidth >= 840.dp
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                TvHeader(modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(18.dp))

                // Center Content: Adaptive layout for TV (wide) vs Compact
                if (isWide) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Left Pane: Technical Specs Card
                        TechnicalSpecsCard(
                            specs = uiState.deviceSpecs,
                            modifier = Modifier.weight(1.1f)
                        )

                        // Right Pane: Gauges & Clean Action Button
                        Column(
                            modifier = Modifier.weight(1.3f),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            MetricGaugesRow(
                                storageStatus = uiState.storageStatus,
                                cpuUsagePercent = uiState.cpuUsagePercent,
                                lastCleanResult = uiState.lastCleanResult,
                                modifier = Modifier.fillMaxWidth()
                            )

                            CleanActionButton(
                                isCleaning = uiState.isCleaning,
                                onCleanClicked = onCleanClicked,
                                focusRequester = buttonFocusRequester,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    // Narrow / Mobile / Portrait layout
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MetricGaugesRow(
                            storageStatus = uiState.storageStatus,
                            cpuUsagePercent = uiState.cpuUsagePercent,
                            lastCleanResult = uiState.lastCleanResult,
                            modifier = Modifier.fillMaxWidth()
                        )

                        CleanActionButton(
                            isCleaning = uiState.isCleaning,
                            onCleanClicked = onCleanClicked,
                            focusRequester = buttonFocusRequester,
                            modifier = Modifier.fillMaxWidth()
                        )

                        TechnicalSpecsCard(
                            specs = uiState.deviceSpecs,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom TV Footer
                TvFooter(
                    versionName = com.example.BuildConfig.VERSION_NAME,
                    developerEmail = "sanchezluys@gmail.com",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Result Dialog
        if (uiState.showResultDialog && uiState.lastCleanResult != null) {
            CleanResultModal(
                result = uiState.lastCleanResult,
                onDismiss = onDismissResult
            )
        }
    }
}
