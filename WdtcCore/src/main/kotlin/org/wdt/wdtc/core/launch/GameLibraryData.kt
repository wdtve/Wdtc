package org.wdt.wdtc.core.launch

import org.wdt.wdtc.core.download.infterface.DownloadSourceInterface
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.LibraryObject
import org.wdt.wdtc.core.game.LibraryObject.NativesOs
import org.wdt.wdtc.core.manger.DownloadSourceManger.downloadSource
import org.wdt.wdtc.core.manger.DownloadSourceManger.isNotOfficialDownloadSource
import org.wdt.wdtc.core.utils.dependency.DependencyDownload
import java.io.File
import java.net.URL

open class GameLibraryData(private val launcher: Launcher) {
  private val source: DownloadSourceInterface = downloadSource

  fun getNativesLibraryFile(nativesOs: NativesOs): File {
    return File(launcher.gameLibraryDirectory, nativesOs.path)
  }

  fun getNativesLibraryUrl(libraryObject: LibraryObject): URL {
    val nativesindows = libraryObject.downloads?.classifiers?.nativesindows
    return if (isNotOfficialDownloadSource) URL(source.libraryUrl + nativesindows?.path) else nativesindows?.url!!
  }

  fun getLibraryFile(libraryObject: LibraryObject): File {
    val libraryPath = launcher.gameLibraryDirectory
    val dependency = DependencyDownload(libraryObject.libraryName!!)
    dependency.downloadPath = libraryPath
    return dependency.libraryFile
  }

  fun getLibraryUrl(libraryObject: LibraryObject): URL {
    return if (isNotOfficialDownloadSource) {
      val dependency = DependencyDownload(libraryObject.libraryName!!)
      dependency.defaultUrl = source.libraryUrl
      dependency.libraryUrl
    } else {
      getOfficialLibraryUrl(libraryObject)
    }
  }

  fun getOfficialLibraryUrl(libraryObject: LibraryObject): URL {
    return libraryObject.downloads?.artifact?.url!!
  }
}
