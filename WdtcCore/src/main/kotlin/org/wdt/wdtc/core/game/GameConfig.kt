package org.wdt.wdtc.core.game

import com.google.gson.annotations.SerializedName
import org.wdt.wdtc.core.utils.runJavaHome

data class GameConfig(
  @field:SerializedName("configVersion")
  var version: Version,
  @field:SerializedName("config")
  var config: Config
) {
  
  @field:SerializedName("__commons__")
  private val commons: String = "Wdtc generates, Do not delete"
  
  constructor(version: Version) : this(version, Config())

  data class Config(
    var memory: Int = 1024,

    var javaPath: String = runJavaHome,

    var width: Int = 1000,

    var hight: Int = 618
  )
}
