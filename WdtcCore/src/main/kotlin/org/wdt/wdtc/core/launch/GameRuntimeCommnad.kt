package org.wdt.wdtc.core.launch

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
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
        if (it.nativesLibrary) {
          ioCoroutineScope.launch(CoroutineName("extract dll file")) {
            unzipByFile(
              it.libraryObject.downloads.classifiers?.nativesWindows.ckeckIsNull().changedNativesLibraryFile,
              version.versionNativesPath.canonicalPath
            )
          }
        } else {
          insertClasspathSeparator(it.libraryObject.changedLibraryFile)
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

