package org.wdt.wdtc.core.impl.launch

import org.wdt.wdtc.core.openapi.auth.preferredUser
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manger.gameConfig
import org.wdt.wdtc.core.openapi.manger.launcherVersion
import org.wdt.wdtc.core.openapi.utils.noNull

class GameCLICommand(private val version: Version) : AbstractGameCommand() {

  override fun getCommand(): StringBuilder {
    version.gameVersionJsonObject.run {
      nonBreakingSpace(mainClass)
      arguments.gameList.noNull().forEach {
        if (it.isJsonPrimitive) {
          nonBreakingSpace(it.asString.replaceData(dataMap))
        }
      }
    }
    version.gameConfig.config.let {
      nonBreakingSpace("--height")
      nonBreakingSpace(it.hight)
      nonBreakingSpace("--width")
      commandBuilder.append(it.width)
    }
    return commandBuilder
  }

  override val dataMap
    get() = mapOf(
      "auth_player_name" to preferredUser.userName,
      "version_name" to version.versionNumber,
      "game_directory" to version.versionDirectory.canonicalPath,
      "assets_root" to version.gameAssetsDirectory.canonicalPath,
      "assets_index_name" to version.gameVersionJsonObject.assets,
      "auth_uuid" to preferredUser.uuid,
      "auth_access_token" to preferredUser.accessToken,
      "user_type" to "mojang",
      "version_type" to "Wdtc-$launcherVersion"
    )
}

