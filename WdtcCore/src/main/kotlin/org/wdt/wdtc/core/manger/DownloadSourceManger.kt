@file:JvmName("DownloadSourceManger")

package org.wdt.wdtc.core.manger

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.download.source.BmclDownloadSource
import org.wdt.wdtc.core.download.source.McbbsDownloadSource
import org.wdt.wdtc.core.download.source.OfficialDownloadSource

val officialDownloadSource: DownloadSourceInterface = OfficialDownloadSource()

val currentDownloadSourceKind: DownloadSourceKind
  get() = currentSetting.downloadSource

val currentDownloadSource: DownloadSourceInterface
  get() {
	  return when (currentDownloadSourceKind) {
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
	get() = currentDownloadSourceKind == DownloadSourceKind.OFFICIAL

val isNotOfficialDownloadSource: Boolean
  get() = !isOfficialDownloadSource

enum class DownloadSourceKind {
  OFFICIAL,
  BMCLAPI,
  MCBBS;
  
}
