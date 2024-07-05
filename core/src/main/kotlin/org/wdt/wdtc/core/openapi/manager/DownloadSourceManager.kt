@file:JvmName("DownloadSourceManager")

package org.wdt.wdtc.core.openapi.manager

import org.wdt.wdtc.core.openapi.download.interfaces.DownloadSourceInterface
import org.wdt.wdtc.core.openapi.download.source.BmclDownloadSource
import org.wdt.wdtc.core.openapi.download.source.McbbsDownloadSource
import org.wdt.wdtc.core.openapi.download.source.OfficialDownloadSource

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
