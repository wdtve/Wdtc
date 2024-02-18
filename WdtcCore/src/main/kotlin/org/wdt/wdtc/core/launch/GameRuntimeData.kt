package org.wdt.wdtc.core.launch

import org.wdt.wdtc.core.game.LibraryObject
import org.wdt.wdtc.core.game.LibraryObject.Artifact
import org.wdt.wdtc.core.game.LibraryObject.Companion.currentNativesOS
import org.wdt.wdtc.core.game.LibraryObject.Companion.officialLibraryUrl
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.manger.downloadSource
import org.wdt.wdtc.core.manger.isNotOfficialDownloadSource
import org.wdt.wdtc.core.utils.ckeckIsNull
import org.wdt.wdtc.core.utils.toURL
import java.io.File
import java.net.URL

open class GameRuntimeData(private val version: Version) {


  val Artifact.changedNativesLibraryFile: File
    get() = File(version.gameLibraryDirectory, this.path)

  val LibraryObject.changedNativesLibraryUrl: URL
    get() = this.downloads.classifiers.currentNativesOS.let {
      return if (isNotOfficialDownloadSource)
        URL(downloadSource.libraryUrl + it.ckeckIsNull().path)
      else
        it.ckeckIsNull().url
    }


  val LibraryObject.changedLibraryFile: File
    get() = version.gameLibraryDirectory.let {
      this.libraryName.apply {
        libraryDirectory = it
      }
    }.libraryFile

  val LibraryObject.changedLibraryUrl: URL
    get() = if (isNotOfficialDownloadSource) {
      this.libraryName.apply {
        libraryRepositoriesUrl = downloadSource.libraryUrl.toURL()
      }.libraryUrl
    } else {
      this.officialLibraryUrl
    }
}


class GameRuntimeList(private val version: Version) {
  val runtimeList: List<GameRuntimeFile>
    get() = ArrayList<GameRuntimeFile>().apply {
      version.gameVersionJsonObject.libraries.forEach {
        val nativesJson = it.natives
        if (nativesJson != null) {
          if (nativesJson.isUseForCurrent) {
            add(GameRuntimeFile(it, true))
          }
        } else {
          val rules = it.rules
          if (rules != null) {
            if (rules.isUseForCurrent) {
              add(GameRuntimeFile(it))
            }
          } else {
            add(GameRuntimeFile(it))
          }
        }
      }
    }

}

class GameRuntimeFile(
  val libraryObject: LibraryObject,
  val nativesLibrary: Boolean = false
)
