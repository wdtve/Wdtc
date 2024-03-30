package org.wdt.wdtc.core.launch

import kotlinx.coroutines.runBlocking
import org.wdt.wdtc.core.auth.accounts.Accounts
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.currentSetting
import org.wdt.wdtc.core.manger.llbmpipeLoader
import org.wdt.wdtc.core.utils.launch
import org.wdt.wdtc.core.utils.noNull
import org.wdt.wdtc.core.utils.unzipByFile
import java.io.File

class GameRuntimeCommnad(private val version: Version) : GameRuntimeData(version) {
	private val commandBuilder = StringBuilder()
	fun getCommand(): StringBuilder = runBlocking {
		runtimeList.forEach {
			it.libraryObject.run {
				if (it.nativesLibrary) {
					downloads.classifiers?.nativesWindows.noNull().changedNativesLibraryFile.also { file ->
						launch("extract ${file.name}") {
							unzipByFile(file, version.versionNativesPath.canonicalPath)
						}
					}
				} else {
					insertClasspathSeparator(changedLibraryFile)
				}
			}
		}
		
		commandBuilder.append(version.versionJar)
		Accounts().jvmCommand.also {
			if (it.isNotEmpty()) {
				commandBuilder.append(it)
			}
		}
		if (currentSetting.llvmpipeLoader) {
			commandBuilder.append(llbmpipeLoaderCommand)
		}
		commandBuilder
	}
	
	private val llbmpipeLoaderCommand: String
		get() = " -javaagent:$llbmpipeLoader"
	
	private fun insertClasspathSeparator(file: File) {
		commandBuilder.append(file.canonicalFile).append(";")
	}
}

