package org.wdt.wdtc.core.launch

import org.wdt.utils.io.createDirectories
import org.wdt.wdtc.core.auth.accounts.Accounts
import org.wdt.wdtc.core.game.GetGameNeedLibraryFile
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.llbmpipeLoader
import org.wdt.wdtc.core.manger.setting
import org.wdt.wdtc.core.utils.getExceptionMessage
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.unzipByFile
import java.io.IOException

class GameClassPath(private val launcher: Launcher) : AbstractGameCommand() {
  override fun getCommand(): StringBuilder {
    try {
      val gameLibraryData = GameLibraryData(launcher)
      launcher.versionNativesPath.createDirectories()
      val fileList = GetGameNeedLibraryFile(launcher)
      for (libraryFile in fileList.libraryList) {
        if (libraryFile.nativesLibrary) {
          unzipByFile(
            gameLibraryData.getNativesLibraryFile(libraryFile.libraryObject.downloads?.classifiers?.nativesindows!!),
            launcher.versionNativesPath.canonicalPath
          )
        } else {
          insertclasspathSeparator(gameLibraryData.getLibraryFile(libraryFile.libraryObject))
        }
      }
      commandBuilder.append(launcher.versionJar)
      val accounts = Accounts().jvmCommand
      if (accounts.isNotEmpty()) {
        commandBuilder.append(accounts)
      }
      if (setting.llvmpipeLoader) {
        commandBuilder.append(llbmpipeLoaderCommand)
      }
    } catch (e: IOException) {
      logmaker.error(e.getExceptionMessage())
    }
    return commandBuilder
  }

  private val llbmpipeLoaderCommand: String
    get() = " -javaagent:$llbmpipeLoader"

}
