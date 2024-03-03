package org.wdt.wdtc.core.launch

import kotlinx.coroutines.runBlocking
import org.wdt.wdtc.core.auth.accounts.Accounts
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.currentSetting
import org.wdt.wdtc.core.manger.llbmpipeLoader
import org.wdt.wdtc.core.utils.*
import java.io.File
import java.io.IOException

class GameRuntimeCommnad(private val version: Version) : GameRuntimeData(version) {
	private val commandBuilder = StringBuilder()
	fun getCommand(): StringBuilder = runBlocking {
		try {
			GameRuntimeList(version).runtimeList.forEach {
				it.libraryObject.run {
					if (it.nativesLibrary) {
						downloads.classifiers?.nativesWindows.noNull().changedNativesLibraryFile.let { file ->
							scwn("extract ${file.name}") {
								unzipByFile(file, version.versionNativesPath.canonicalPath)
							}
						}
					} else {
						insertClasspathSeparator(changedLibraryFile)
					}
				}
			}
			
			commandBuilder.append(version.versionJar)
			Accounts.jvmCommand.let {
				if (it.isNotEmpty()) {
					commandBuilder.append(it)
				}
			}
			if (currentSetting.llvmpipeLoader) {
				commandBuilder.append(llbmpipeLoaderCommand)
			}
		} catch (e: IOException) {
			logmaker.error(e.getExceptionMessage())
		}
		commandBuilder
	}
	
	private val llbmpipeLoaderCommand: String
		get() = " -javaagent:$llbmpipeLoader"
	
	private fun insertClasspathSeparator(file: File) {
		commandBuilder.append(file.canonicalFile).append(";")
	}
}

