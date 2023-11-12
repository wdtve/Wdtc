package org.wdt.wdtc.core.download.infterface

import java.io.IOException

interface InstallTaskInterface {
  @Throws(IOException::class)
  fun overwriteVersionJson()

  @Throws(IOException::class)
  fun writeVersionJsonPatches()

  @Throws(IOException::class)
  fun afterDownloadTask()

  @Throws(IOException::class)
  fun beforInstallTask()
}
