package org.wdt.wdtc.core.download.infterface

import org.wdt.wdtc.core.utils.KindOfMod

interface ModInstallTaskInterface {
	fun overwriteVersionJson()
	
	fun writeVersionJsonPatches()
	
	suspend fun afterDownloadTask()
	
	suspend fun beforInstallTask()
}

interface ModDownloadInfoInterface {
	val modVersion: String
	val modInstallTask: ModInstallTaskInterface
	val modKind: KindOfMod
	override fun toString(): String
	override fun equals(other: Any?): Boolean
	override fun hashCode(): Int
}

