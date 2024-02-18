package org.wdt.wdtc.core.download.infterface

import org.wdt.wdtc.core.utils.KindOfMod
import java.io.IOException

interface ModInstallTaskInterface {
  @Throws(IOException::class)
  fun overwriteVersionJson()

  @Throws(IOException::class)
  fun writeVersionJsonPatches()

  @Throws(IOException::class)
  fun afterDownloadTask()

  @Throws(IOException::class)
  fun beforInstallTask()
}

interface ModDownloadInfoInterface {
  val modVersion: String
  val modInstallTask: ModInstallTaskInterface
  val modKind: KindOfMod
  override fun toString(): String
}

