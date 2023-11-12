package org.wdt.wdtc.core.launch

import org.wdt.utils.io.FileUtils
import org.wdt.wdtc.core.game.GetGameNeedLibraryFile
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.manger.FileManger.llbmpipeLoader
import org.wdt.wdtc.core.manger.SettingManger.Companion.setting
import org.wdt.wdtc.core.utils.ZipUtils.unzipByFile
import java.io.IOException

class GameClassPath(private val launcher: Launcher) : AbstractGameCommand() {
  override fun getCommand(): StringBuilder {
    try {
      val gameLibraryData = GameLibraryData(launcher)
      FileUtils.createDirectories(launcher.versionNativesPath)
      val fileList = GetGameNeedLibraryFile(launcher)
      for (libraryFile in fileList.fileList) {
        if (libraryFile.nativesLibrary) {
          unzipByFile(
            gameLibraryData.getNativesLibraryFile(libraryFile.libraryObject.downloads?.classifiers?.nativesindows!!),
            FileUtils.getCanonicalPath(
              launcher.versionNativesPath
            )
          )
        } else {
          InsertclasspathSeparator(gameLibraryData.getLibraryFile(libraryFile.libraryObject))
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
      throw RuntimeException(e)
    }
    return commandBuilder
  }

  private val llbmpipeLoaderCommand: String
    get() = " -javaagent:$llbmpipeLoader"

}
