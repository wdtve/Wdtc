package org.wdt.wdtc.core.game.config

import com.google.gson.annotations.SerializedName
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.utils.JavaUtils.runJavaHome


class DefaultGameConfig(launcher: Launcher) {
  @SerializedName("info")
  var info: VersionInfo? = null

  @SerializedName("config")
  var config: Config? = null

  init {
    info = launcher.versionInfo
    config = Config()
  }


  override fun toString(): String {
    return "DefaultGameConfig(info=$info, config=$config)"
  }

  class Config @JvmOverloads constructor(
    var memory: Int = 1024,

    var javaPath: String = runJavaHome,

    var width: Int = 1000,

    var hight: Int = 618
  ) {


    override fun toString(): String {
      return "Config(Memory=$memory, JavaPath='$javaPath', width=$width, hight=$hight)"
    }

  }

}
