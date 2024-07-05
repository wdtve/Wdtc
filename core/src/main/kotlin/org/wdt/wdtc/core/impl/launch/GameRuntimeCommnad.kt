package org.wdt.wdtc.core.impl.launch

import kotlinx.coroutines.runBlocking
import org.wdt.wdtc.core.openapi.auth.AccountsType
import org.wdt.wdtc.core.openapi.auth.preferredUser
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manager.authlibInjector
import org.wdt.wdtc.core.openapi.manager.currentSetting
import org.wdt.wdtc.core.openapi.manager.littleskinApiUrl
import org.wdt.wdtc.core.openapi.manager.llbmpipeLoader
import org.wdt.wdtc.core.openapi.utils.STRING_SPACE
import org.wdt.wdtc.core.openapi.utils.launch
import org.wdt.wdtc.core.openapi.utils.noNull
import org.wdt.wdtc.core.openapi.utils.unzipByFile
import java.io.File

class GameRuntimeCommnad(private val version: Version) : GameRuntimeData(version) {
	
	private val commandBuilder = StringBuilder()
	
	fun getCommand(): StringBuilder = runBlocking {
		runtimeList.forEach {
			if (it.isNatives) {
				it.library.downloads.classifiers?.nativesWindows.noNull().changedNativesLibraryFile.also { file ->
					launch("extract ${file.name}") { unzipByFile(file, version.versionNativesPath.canonicalPath) }
				}
			} else {
				commandBuilder.appendWith(it.library.changedLibraryFile)
			}
		}
		commandBuilder.apply {
			append(version.versionJar)
			if (preferredUser.type == AccountsType.YGGDRASIL) {
				append(buildString {
					append(STRING_SPACE)
					append("-javaagent:")
					append(authlibInjector.canonicalPath)
					append("=")
					append(littleskinApiUrl)
					append(STRING_SPACE)
					append("-Dauthlibinjector.yggdrasil.prefetched=")
					append(preferredUser.base64Data)
					
				})
			}
			if (currentSetting.llvmpipeLoader) {
				append(llbmpipeLoaderCommand)
			}
		}
	}
	
	private val llbmpipeLoaderCommand: String
		get() = " -javaagent:$llbmpipeLoader"
	
	private fun StringBuilder.appendWith(file: File) {
		append(file.canonicalFile).append(";")
	}
}

