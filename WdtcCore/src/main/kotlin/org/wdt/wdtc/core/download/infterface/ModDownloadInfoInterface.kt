package org.wdt.wdtc.core.download.infterface

import org.wdt.wdtc.core.utils.KindOfMod

interface ModDownloadInfoInterface {
  val modVersion: String
  val modInstallTask: InstallTaskInterface
  val modKind: KindOfMod
  override fun toString(): String
}
