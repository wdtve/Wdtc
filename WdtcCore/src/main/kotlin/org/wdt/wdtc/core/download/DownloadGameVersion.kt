package org.wdt.wdtc.core.download

import org.wdt.wdtc.core.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.game.*
import java.io.IOException

open class DownloadGameVersion @JvmOverloads constructor(
    protected val launcher: Launcher,
    protected val install: Boolean = false
) {
  protected val downloadGame: DownloadVersionGameFile = DownloadVersionGameFile(launcher, install)

  @Throws(IOException::class)
  fun downloadGameFileTask() {
    downloadGame.startDownloadGameVersionJson()
    downloadGame.startDownloadGameAssetsListJson()
    downloadGame.startDownloadVersionJar()
  }

  fun downloadGameLibraryTask() {
    downloadGame.downloadGameLibraryFileTask.startDownloadLibraryFile()
  }

  fun downloadResourceFileTask() {
    downloadGame.downloadGameAssetsFile.startDownloadAssetsFiles()
  }

  @Throws(IOException::class)
  fun startDownloadGame() {
    downloadGameFileTask()
    downloadGameLibraryTask()
    downloadResourceFileTask()
  }
}
