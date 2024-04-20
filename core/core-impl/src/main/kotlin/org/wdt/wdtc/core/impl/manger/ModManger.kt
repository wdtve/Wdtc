@file:JvmName("ModManger")

package org.wdt.wdtc.core.impl.manger

import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.impl.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.impl.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.impl.download.quilt.QuiltInstallTask
import org.wdt.wdtc.core.openapi.game.Version
import java.io.IOException
import java.util.regex.Pattern

fun setModTask(version: Version): Version? {
	return version.run {
		try {
			if (this.versionJson.isFileNotExists()) {
				return null
			}
			Pattern.compile("(.+?)-(.+?)-(.+)").matcher(this.gameVersionJsonObject.id).let {
				if (it.find()) {
					val modVersion = it.group(3)
					when (it.group(2)) {
						"forge" -> this.modDownloadInfo = ForgeDownloadInfo(this, modVersion)
						"fabric" -> this.modDownloadInfo = FabricDonwloadInfo(this, modVersion)
						"quilt" -> this.modDownloadInfo = QuiltInstallTask(this, modVersion)
					}
				}
			}
			this
		} catch (e: IOException) {
			null
		}
	}
}
