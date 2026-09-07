package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.system.FormatUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("CleanCache SmarTV", appName)
  }

  @Test
  fun `format bytes test`() {
    val formattedGb = FormatUtils.formatBytes(5L * 1024 * 1024 * 1024)
    assertTrue(formattedGb.contains("GB"))
    val formattedMb = FormatUtils.formatBytes(250L * 1024 * 1024)
    assertTrue(formattedMb.contains("MB"))
  }
}
