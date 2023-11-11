package org.wdt.wdtc.core.manger

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface

object URLManger {
    const val BMCALAPI_COM = "https://download.mcbbs.net/"
    const val ALIYUN_MAVEN = "https://maven.aliyun.com/repository/public/"

    @JvmStatic
    val pistonDataMojang: String
        get() = DownloadSourceInterface.PISTON_DATA_MOJANG

    @JvmStatic
    val pistonMetaMojang: String
        get() = DownloadSourceInterface.PISTON_META_MOJANG

    @JvmStatic
    val littleskinUrl: String
        get() = "https://littleskin.cn"
    val mojangLibraries: String
        get() = DownloadSourceInterface.MOJANG_LIBRARIES

    @JvmStatic
    val littleskinApi: String
        get() = "$littleskinUrl/api/yggdrasil"
}
