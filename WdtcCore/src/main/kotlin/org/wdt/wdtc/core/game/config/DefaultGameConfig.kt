package org.wdt.wdtc.core.game.config

import com.google.gson.annotations.SerializedName
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.utils.runJavaHome

data class DefaultGameConfig(
  @field:SerializedName("info")
  var info: VersionInfo,

  @field:SerializedName("config")
  var config: Config

) {
  constructor(launcher: Launcher) : this(launcher.versionInfo, Config())

  data class Config @JvmOverloads constructor(
    var memory: Int = 1024,

    var javaPath: String = runJavaHome,

    var width: Int = 1000,

    var hight: Int = 618
  )
}
