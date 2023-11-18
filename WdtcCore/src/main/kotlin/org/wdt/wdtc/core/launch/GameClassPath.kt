package org.wdt.wdtc.core.launch

import org.wdt.utils.io.createDirectories
import org.wdt.wdtc.core.game.GetGameNeedLibraryFile
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.FileManger.llbmpipeLoader
import org.wdt.wdtc.core.manger.SettingManger.Companion.setting
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage
import org.wdt.wdtc.core.utils.WdtcLogger.getWdtcLogger
import org.wdt.wdtc.core.utils.ZipUtils.unzipByFile
import java.io.IOException

class GameClassPath(private val launcher: Launcher) : AbstractGameCommand() {
  private val logmaker = GameClassPath::class.java.getWdtcLogger()
  override fun getCommand(): StringBuilder {
    try {
      val gameLibraryData = GameLibraryData(launcher)
      launcher.versionNativesPath.createDirectories()
      val fileList = GetGameNeedLibraryFile(launcher)
      for (libraryFile in fileList.fileList) {
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
      val accounts = launcher.accounts.jvmCommand
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
