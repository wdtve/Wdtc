package org.wdt.wdtc.core.download.game

import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.isFileNotExistsAndIsNotSameSize
import org.wdt.utils.io.writeStringToFile
import org.wdt.wdtc.core.download.SpeedOfProgress
import org.wdt.wdtc.core.game.GetGameNeedLibraryFile
import org.wdt.wdtc.core.game.Launcher
import org.wdt.wdtc.core.game.LibraryObject
import org.wdt.wdtc.core.launch.GameLibraryData
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.startDownloadTask
import org.wdt.wdtc.core.utils.startThread
import java.io.IOException
import java.nio.file.Files

class DownloadGameClass(private val launcher: Launcher) : GameLibraryData(launcher) {
  fun startDownloadLibraryFile() {
    try {
      Files.createDirectories(launcher.versionNativesPath.toPath())
      val fileList = GetGameNeedLibraryFile(launcher).libraryList
      val speed = SpeedOfProgress(fileList.size)
      for (libraryFile in fileList) {
        if (libraryFile.nativesLibrary) {
          startDownloadNativesLibTask(libraryFile.libraryObject, speed)
        } else {
          startDownloadLibraryTask(libraryFile.libraryObject, speed)
        }
      }
      speed.await()
    } catch (e: IOException) {
      logmaker.error("Download Library File Error,", e)
    }
    try {
      launcher.versionLog4j2.writeStringToFile(
        IOUtils.toString(
          DownloadGameClass::class.java.getResourceAsStream(
            "/assets/log4j2.xml"
          )
        )
      )
    } catch (e: IOException) {
      logmaker.error("log4j.xml not found", e)
    }
  }

  fun startDownloadLibraryTask(libraryObject: LibraryObject, speed: SpeedOfProgress) {
    val libraryFile = getLibraryFile(libraryObject)
    if (libraryFile.isFileNotExistsAndIsNotSameSize(libraryObject.downloads?.artifact?.size!!)) {
      Runnable {
        startDownloadTask(getLibraryUrl(libraryObject), libraryFile)
        speed.countDown()
      }.startThread()
    } else {
      speed.countDown()
    }
  }

  fun startDownloadNativesLibTask(libraryObject: LibraryObject, speed: SpeedOfProgress) {
    val nativesWindows = libraryObject.downloads?.classifiers?.nativesindows!!
    val nativesLibrary = getNativesLibraryFile(nativesWindows)
    if (nativesLibrary.isFileNotExistsAndIsNotSameSize(nativesWindows.size.toLong())) {
      Runnable {
        startDownloadTask(getNativesLibraryUrl(libraryObject), nativesLibrary)
        speed.countDown()
      }.startThread()
    } else {
      speed.countDown()
    }
  }
}
