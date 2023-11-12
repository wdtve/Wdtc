package org.wdt.wdtc.core.launch

import org.wdt.wdtc.core.auth.User
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.VMManger.launcherVersion
import java.io.IOException

class GameCLICommand(private val launcher: Launcher) : AbstractGameCommand() {
  @Throws(IOException::class)
  override fun getCommand(): StringBuilder {
    val gameConfig = launcher.gameConfig.config!!
    val versionJsonObject = launcher.gameVersionJsonObject
    NonBreakingSpace(versionJsonObject.mainClass)
    for (element in versionJsonObject.arguments?.gameList!!) {
      if (!element.isJsonObject) {
        NonBreakingSpace(replaceData(element.asString))
      }
    }
    NonBreakingSpace("--height")
    NonBreakingSpace(gameConfig.hight)
    NonBreakingSpace("--width")
    commandBuilder.append(gameConfig.width)
    return commandBuilder
  }

  @get:Throws(IOException::class)
  private val dataMap: Map<String, String>
    get() {
      val user = User.user
      return java.util.Map.of(
        "\${auth_player_name}",
        user.userName,
        "\${version_name}",
        launcher.versionNumber,
        "\${game_directory}",
        launcher.versionDirectory.canonicalPath,
        "\${assets_root}",
        launcher.gameAssetsDirectory.canonicalPath,
        "\${assets_index_name}",
        launcher.gameVersionJsonObject.assets,
        "\${auth_uuid}",
        user.uuid,
        "\${auth_access_token}",
        user.accessToken,
        "\${user_type}",
        user.type.toString(),
        "\${version_type}",
        "Wdtc-$launcherVersion"
      )
    }

  @Throws(IOException::class)
  private fun replaceData(strs: String): String {
    var str = strs
    val replaceMap = dataMap
    for (s in replaceMap.keys) {
      str = str.replace(s, replaceMap[s]!!)
    }
    return str
  }
}
