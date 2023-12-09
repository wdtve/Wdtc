package org.wdt.wdtc.core.launch

import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.LibraryObject
import org.wdt.wdtc.core.game.LibraryObject.NativesOs
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.manger.isNotOfficialDownloadSource
import org.wdt.wdtc.core.utils.dependency.DependencyDownload
import java.io.File
import java.net.URL

open class GameLibraryData(private val launcher: Launcher) {

  fun getNativesLibraryFile(nativesOs: NativesOs): File {
    return File(launcher.gameLibraryDirectory, nativesOs.path!!)
  }

  fun getNativesLibraryUrl(libraryObject: LibraryObject): URL {
    val nativesindows = libraryObject.downloads?.classifiers?.nativesindows
    return if (isNotOfficialDownloadSource) URL(downloadSource.libraryUrl + nativesindows?.path) else nativesindows?.url!!
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
      dependency.defaultUrl = downloadSource.libraryUrl
      dependency.libraryUrl
    } else {
      getOfficialLibraryUrl(libraryObject)
    }
  }

  fun getOfficialLibraryUrl(libraryObject: LibraryObject): URL {
    return libraryObject.downloads?.artifact?.url!!
  }
}
