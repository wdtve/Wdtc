package org.wdt.wdtc.core.impl.launch

import org.wdt.wdtc.core.openapi.game.LibraryObject
import org.wdt.wdtc.core.openapi.game.LibraryObject.Artifact
import org.wdt.wdtc.core.openapi.game.LibraryObject.Companion.currentNativesOS
import org.wdt.wdtc.core.openapi.game.LibraryObject.Companion.officialLibraryUrl
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manager.currentDownloadSource
import org.wdt.wdtc.core.openapi.manager.isNotOfficialDownloadSource
import org.wdt.wdtc.core.openapi.utils.noNull
import org.wdt.wdtc.core.openapi.utils.toURL
import java.io.File
import java.net.URL

open class GameRuntimeData(private val version: Version) {
	
	val Artifact.changedNativesLibraryFile: File
		get() = File(version.gameLibraryDirectory, this.path)
	
	val LibraryObject.changedNativesLibraryURL: URL
		get() = this.downloads.classifiers.currentNativesOS.noNull().let {
			if (isNotOfficialDownloadSource)
				URL(currentDownloadSource.libraryURL + it.path)
			else
				it.url
		}
	
	
	val LibraryObject.changedLibraryFile: File
		get() = version.gameLibraryDirectory.let {
			this.libraryName.apply {
				libraryDirectory = it
			}
		}.libraryFile
	
	val LibraryObject.changedLibraryURL: URL
		get() = if (isNotOfficialDownloadSource) {
			this.libraryName.apply {
				libraryRepositoriesUrl = currentDownloadSource.libraryURL.toURL()
			}.libraryUrl
		} else {
			this.officialLibraryUrl
		}
	
	val runtimeList: List<GameRuntimeFile> by lazy {
		ArrayList<GameRuntimeFile>().apply {
			version.gameVersionJsonObject.libraries.forEach {
				val nativesJson = it.natives
				if (nativesJson != null) {
					if (nativesJson.isUseForCurrent) {
						add(GameRuntimeFile(it, true))
					}
				} else {
					val rules = it.rules
					if (rules != null) {
						if (rules.isUseForCurrent) {
							add(GameRuntimeFile(it))
						}
					} else {
						add(GameRuntimeFile(it))
					}
				}
			}
		}
	}
	
}

class GameRuntimeFile(
	val library: LibraryObject,
	val isNatives: Boolean = false
)

