@file:JvmName("ModManger")

package org.wdt.wdtc.core.impl.manger

import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.impl.download.fabric.FabricVersionImpl
import org.wdt.wdtc.core.impl.download.forge.ForgeVersionImpl
import org.wdt.wdtc.core.impl.download.quilt.QuiltVersionImpl
import org.wdt.wdtc.core.openapi.download.game.VersionNotFoundException
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.utils.logmaker
import java.util.regex.Pattern

fun setModTask(version: Version): Version? {
	if (version.versionJson.isFileNotExists()) {
		return null
	}
	return version.runCatching {
		val matcher = Pattern.compile("(.+?)(-(.+?)-(.+))?").matcher(this.gameVersionJsonObject.id).also {
			assert(it.find())
		}
		if (matcher.group(2) == null) {
			return this
		}
		when (matcher.group(3).also {
			logmaker.info(it ?: "null3")
		}) {
			"forge" -> ForgeVersionImpl(this, matcher.group(4))
			"fabric" -> FabricVersionImpl(this, matcher.group(4))
			"quilt" -> QuiltVersionImpl(this, matcher.group(4))
			else -> {
				throw VersionNotFoundException("impossable is other")
			}
		}
	}.getOrThrow()
}

