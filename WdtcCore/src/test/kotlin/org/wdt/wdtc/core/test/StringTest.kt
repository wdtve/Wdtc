package org.wdt.wdtc.core.test

import org.wdt.wdtc.core.utils.STRING_SPACE
import org.wdt.wdtc.core.utils.cleanStrInString
import kotlin.test.Test

class StringTest {
  @Test
  fun testReagx() {
	  println("-DFabricMcEmu= net.minecraft.client.main.Main ".cleanStrInString(STRING_SPACE))
  }

	val string = """
		   {
    "versionNumber": "1.20.4",
    "workDirectory": "E:\\Wdtc",
    "versionDirectory": "E:\\Wdtc\\.minecraft\\versions\\1.20.4",
    "configFile": "E:\\Wdtc\\.minecraft\\versions\\1.20.4\\config.json",
    "modKind": "FABRIC",
    "fabricDownlaodInfo": {
      "modKind": "FABRIC",
      "modVersionNumber": "0.15.7"
    },
    "forgeDownloadInfo": null,
    "quiltDownloadInfo": null
  }
	""".trimIndent()
  @Test
  fun testName() {
  }

}