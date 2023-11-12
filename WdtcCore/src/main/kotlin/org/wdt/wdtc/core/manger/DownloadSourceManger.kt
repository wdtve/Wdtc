package org.wdt.wdtc.core.manger

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.download.source.BmclDownloadSource
import org.wdt.wdtc.core.download.source.McbbsDownloadSource
import org.wdt.wdtc.core.download.source.OfficialDownloadSource

object DownloadSourceManger {
  @JvmStatic
  val officialDownloadSource: DownloadSourceInterface
    get() = OfficialDownloadSource()

  @JvmStatic
  val downloadSourceKind: DownloadSourceList
    get() = SettingManger.setting.downloadSource

  @JvmStatic
  val downloadSource: DownloadSourceInterface
    get() {
      val source = downloadSourceKind
      return when (source) {
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

  @JvmStatic
  val isNotOfficialDownloadSource: Boolean
    get() = !isOfficialDownloadSource()

  @JvmStatic
  fun isOfficialDownloadSource(): Boolean {
    return downloadSourceKind == DownloadSourceList.OFFICIAL
  }

  enum class DownloadSourceList {
    OFFICIAL,
    BMCLAPI,
    MCBBS
  }
}
