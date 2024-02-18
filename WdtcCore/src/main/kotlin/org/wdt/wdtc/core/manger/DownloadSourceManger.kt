@file:JvmName("DownloadSourceManger")

package org.wdt.wdtc.core.manger

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.download.source.BmclDownloadSource
import org.wdt.wdtc.core.download.source.McbbsDownloadSource
import org.wdt.wdtc.core.download.source.OfficialDownloadSource

val officialDownloadSource: DownloadSourceInterface = OfficialDownloadSource()

val downloadSourceKind: DownloadSourceList = currentSetting.downloadSource

val downloadSource: DownloadSourceInterface
  get() {
    return when (downloadSourceKind) {
      DownloadSourceList.OFFICIAL -> {
        OfficialDownloadSource()
      }

      DownloadSourceList.BMCLAPI -> {
        BmclDownloadSource()
      }

      else -> {
        McbbsDownloadSource()
      }
    }
  }

val isOfficialDownloadSource: Boolean = downloadSourceKind == DownloadSourceList.OFFICIAL

val isNotOfficialDownloadSource: Boolean = !isOfficialDownloadSource

enum class DownloadSourceList {
  OFFICIAL,
  BMCLAPI,
  MCBBS
}
