package org.wdt.wdtc.core.download.liteloader

import org.wdt.wdtc.core.download.game.VersionNotFoundException
import org.wdt.wdtc.core.download.infterface.VersionJsonObjectInterface
import org.wdt.wdtc.core.download.infterface.VersionListInterface

class LiteloaderVersionList : VersionListInterface {
  override val versionList: Set<VersionJsonObjectInterface>
    get() = throw VersionNotFoundException("Not version")
}
