package org.wdt.wdtc.core.openapi.download.interfaces

import com.google.gson.annotations.JsonAdapter
import org.wdt.wdtc.core.openapi.manger.KindOfMod
import org.wdt.wdtc.core.openapi.utils.gson.DownloadInfoTypeAdapter

interface ModInstallTaskInterface {
	fun overwriteVersionJson()
	
	fun writeVersionJsonPatches()
	
	suspend fun afterDownloadTask()
	
	suspend fun beforInstallTask()
}

@JsonAdapter(DownloadInfoTypeAdapter::class)
interface ModDownloadInfoInterface {
	val modVersion: String
	val modInstallTask: ModInstallTaskInterface
	val modKind: KindOfMod
	override fun toString(): String
	override fun equals(other: Any?): Boolean
	override fun hashCode(): Int
}

