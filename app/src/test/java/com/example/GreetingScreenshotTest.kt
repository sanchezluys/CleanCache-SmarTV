package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.model.DeviceSpecs
import com.example.model.StorageStatus
import com.example.ui.CleanCacheScreen
import com.example.ui.MainUiState
import com.example.ui.theme.CleanCacheTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    val sampleSpecs = DeviceSpecs(
      processorName = "Quad-Core ARM Cortex-A55",
      cpuCores = 4,
      cpuArchitecture = "arm64-v8a",
      totalRamBytes = 2L * 1024 * 1024 * 1024,
      availRamBytes = 900L * 1024 * 1024,
      usedRamBytes = 1148L * 1024 * 1024,
      ramUsagePercent = 56f,
      totalRomBytes = 16L * 1024 * 1024 * 1024,
      availRomBytes = 6L * 1024 * 1024 * 1024,
      usedRomBytes = 10L * 1024 * 1024 * 1024,
      romUsagePercent = 62.5f
    )
    val sampleStorage = StorageStatus(
      totalBytes = 16L * 1024 * 1024 * 1024,
      availableBytes = 6L * 1024 * 1024 * 1024,
      usedBytes = 10L * 1024 * 1024 * 1024,
      usagePercent = 62.5f
    )
    val testState = MainUiState(
      deviceSpecs = sampleSpecs,
      storageStatus = sampleStorage,
      cpuUsagePercent = 28f
    )

    composeTestRule.setContent {
      CleanCacheTheme {
        CleanCacheScreen(
          uiState = testState,
          onCleanClicked = {},
          onDismissResult = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
