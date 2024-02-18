@file:JvmName("URLManger")

package org.wdt.wdtc.core.manger

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.utils.toURL
import java.net.URL

const val BMCALAPI_COM = "https://download.mcbbs.net/"
const val ALIYUN_MAVEN = "https://maven.aliyun.com/repository/public/"

val pistonDataMojang: String
  get() = DownloadSourceInterface.PISTON_DATA_MOJANG

val pistonMetaMojang: String
  get() = DownloadSourceInterface.PISTON_META_MOJANG

val littleskinUrl: String
  get() = "https://littleskin.cn"
val mojangLibrariesUrl: String
  get() = DownloadSourceInterface.MOJANG_LIBRARIES

val littleskinApiUrl: URL
  get() = "$littleskinUrl/api/yggdrasil".toURL()

