package org.wdt.wdtc.core.download.liteloader

import org.wdt.wdtc.core.download.game.GameVersionsObjectList
import org.wdt.wdtc.core.download.game.VersionNotFoundException
import org.wdt.wdtc.core.download.infterface.VersionListInterface
import org.wdt.wdtc.core.download.infterface.VersionsJsonObjectInterface

class LiteloaderVersionList : VersionListInterface {
  override val versionList: GameVersionsObjectList
    get() = throw VersionNotFoundException("Not version")

  class LiteloaderVersionsJsonObjectImpl : VersionsJsonObjectInterface {
    override val versionNumber: String
      get() = TODO("Not yet implemented")

    override fun hashCode(): Int {
      TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
      TODO("Not yet implemented")
    }

  }
}
