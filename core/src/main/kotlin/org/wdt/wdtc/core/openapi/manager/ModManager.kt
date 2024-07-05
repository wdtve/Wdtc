package org.wdt.wdtc.core.openapi.manager

import org.wdt.wdtc.core.impl.download.fabric.FabricVersionImpl
import org.wdt.wdtc.core.impl.download.forge.ForgeVersionImpl
import org.wdt.wdtc.core.impl.download.quilt.QuiltVersionImpl
import org.wdt.wdtc.core.openapi.game.Version

val Version.isForge: Boolean
	get() = this is ForgeVersionImpl


val Version.isFabric: Boolean
	get() = this is FabricVersionImpl


val Version.isQuilt
	get() = this is QuiltVersionImpl

fun Version.setModTask(): Version? {
	return org.wdt.wdtc.core.impl.manger.setModTask(this)
}

enum class KindOfMod {
	ORIGINAL,
	FABRIC,
	FABRICAPI,
	FORGE,
	QUILT,
	OTHER
}