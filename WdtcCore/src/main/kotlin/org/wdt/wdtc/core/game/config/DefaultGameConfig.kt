package org.wdt.wdtc.core.game.config

import com.google.gson.annotations.SerializedName
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.utils.runJavaHome

data class DefaultGameConfig(
  @field:SerializedName("info")
  var info: VersionInfo,

  @field:SerializedName("config")
  var config: Config

) {
  constructor(version: Version) : this(version.versionInfo, Config())

  data class Config(
    var memory: Int = 1024,

    var javaPath: String = runJavaHome,

    var width: Int = 1000,

    var hight: Int = 618
  )
}
