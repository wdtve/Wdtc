package org.wdt.wdtc.core.openapi.manger

import org.wdt.wdtc.core.openapi.download.interfaces.ModInstallTaskInterface
import org.wdt.wdtc.core.openapi.game.Version

val Version.isForge: Boolean
	get() = this.kind == KindOfMod.FORGE


val Version.isFabric: Boolean
	get() = this.kind == KindOfMod.FABRIC


val Version.isQuilt
	get() = this.kind == KindOfMod.QUILT

val Version.modInstallTask: ModInstallTaskInterface?
	get() = this.modDownloadInfo?.modInstallTask

fun Version.setModTask(): Version? {
	return Class.forName("org.wdt.wdtc.core.impl.manger.ModManger").let {
		it.getDeclaredMethod("setModTask", Version::class.java).run {
			invoke(null, this@setModTask) as Version?
		}
	}
}

enum class KindOfMod {
	ORIGINAL,
	FABRIC,
	FABRICAPI,
	FORGE,
	QUILT
}