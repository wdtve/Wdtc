package org.wdt.wdtc.core.test

import org.wdt.wdtc.core.download.forge.ForgeInstallTask
import org.wdt.wdtc.core.download.forge.ForgeVersionList
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.runStartUpTask
import org.wdt.wdtc.core.utils.initLogmaker
import kotlin.test.Test

class ForgeTest {
  private val version = Version("1.17.1")

  @Test
  fun printVersions() {
    ForgeVersionList(version).versionList.forEach {
      println(it.versionNumber)
    }
  }
  @Test
  fun downloadMeteFile() {
    runStartUpTask()
    initLogmaker()
    val task = ForgeInstallTask(version, "37.1.1")
    task.beforInstallTask()
  }
}