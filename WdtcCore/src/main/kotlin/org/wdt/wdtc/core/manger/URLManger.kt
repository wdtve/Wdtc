@file:JvmName("URLManger")

package org.wdt.wdtc.core.manger

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.utils.toURL
import java.net.URL

const val BMCALAPI_COM = "https://download.mcbbs.net/"
const val ALIYUN_MAVEN = "https://maven.aliyun.com/repository/public/"

const val pistonDataMojang: String = DownloadSourceInterface.PISTON_DATA_MOJANG

const val pistonMetaMojang: String = DownloadSourceInterface.PISTON_META_MOJANG

const val littleskinUrl: String = "https://littleskin.cn"

const val mojangLibrariesUrl: String = DownloadSourceInterface.MOJANG_LIBRARIES

val littleskinApiUrl: URL = "$littleskinUrl/api/yggdrasil".toURL()

