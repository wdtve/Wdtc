package org.wdt.wdtc.core.manger

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.isFileOlder
import org.wdt.wdtc.core.download.game.DownloadVersionGameFile
import org.wdt.wdtc.core.game.GameVersionJsonObject
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


open class GameFileManger : GameDirectoryManger {
  val versionNumber: String

  constructor(versionNumber: String) {
    this.versionNumber = versionNumber
  }

  constructor(versionNumber: String, here: File) : super(here) {
    this.versionNumber = versionNumber
  }

  val versionDirectory: File
    get() = File(gameVersionsDirectory, versionNumber)
  val versionJson: File
    get() = File(versionDirectory, "$versionNumber.json")
  val versionJar: File
    get() = File(versionDirectory, "$versionNumber.jar")
  val versionLog4j2: File
    get() = File(versionDirectory, "log4j2.xml")
  val versionNativesPath: File
    get() = File(versionDirectory, "natives-windows-x86_64")

  @get:Throws(IOException::class)
  val gameAssetsListJson: File
    get() = File(gameAssetsDirectory, "indexes/${gameVersionJsonObject.assets}.json")
  val gameOptionsFile: File
    get() = File(versionDirectory, "assets/options.txt")
  val gameModsPath: File
    get() = File(versionDirectory, "mods")
  val gameLogDirectory: File
    get() = File(versionDirectory, "logs")

  fun putToVersionJson(o: GameVersionJsonObject) {
    versionJson.writeObjectToFile(o)
  }

  @get:Throws(IOException::class)
  val gameVersionJsonObject: GameVersionJsonObject
    get() {
      if (versionJson.isFileNotExists()) {
        throw FileNotFoundException("$versionJson not exists")
      }
      return versionJson.readFileToClass()
    }
  val laucnherProfiles: File
    get() = File(gameDirectory, "Launcher_profiles.json")
  val versionConfigFile: File
    get() = File(versionDirectory, "config.json")
}

fun downloadVersionManifestJsonFileTask() {
  val calendar = Calendar.getInstance()
  calendar.time = Date()
  calendar.add(Calendar.DATE, -7)
  if (versionManifestFile.isFileNotExists() ||
    versionManifestFile.isFileOlder(calendar.time)
  ) {
    runBlocking {
      launch {
        DownloadVersionGameFile.startDownloadVersionManifestJsonFile()
      }
    }
  }
}