package org.wdt.wdtc.core.impl.download.liteloader

import org.wdt.wdtc.core.openapi.download.game.VersionNotFoundException
import org.wdt.wdtc.core.openapi.download.interfaces.VersionListInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsJsonObjectInterface
import org.wdt.wdtc.core.openapi.download.interfaces.VersionsObjectSequence

class LiteloaderVersionList : VersionListInterface {
	override suspend fun getVersionList(): VersionsObjectSequence {
		throw VersionNotFoundException("Not version")
	}

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
