@file:JvmName("DownloadSourceManger")

package org.wdt.wdtc.core.manger

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.download.source.BmclDownloadSource
import org.wdt.wdtc.core.download.source.McbbsDownloadSource
import org.wdt.wdtc.core.download.source.OfficialDownloadSource

val officialDownloadSource: DownloadSourceInterface = OfficialDownloadSource()

val downloadSourceKind: DownloadSourceKind
  get() = currentSetting.downloadSource

val downloadSource: DownloadSourceInterface
  get() {
    return when (downloadSourceKind) {
      DownloadSourceKind.OFFICIAL -> {
        OfficialDownloadSource()
      }

      DownloadSourceKind.BMCLAPI -> {
        BmclDownloadSource()
      }

      else -> {
        McbbsDownloadSource()
      }
    }
  }

val isOfficialDownloadSource: Boolean
  get() = downloadSourceKind == DownloadSourceKind.OFFICIAL

val isNotOfficialDownloadSource: Boolean
  get() = !isOfficialDownloadSource

enum class DownloadSourceKind {
  OFFICIAL,
  BMCLAPI,
  MCBBS;
  
}
