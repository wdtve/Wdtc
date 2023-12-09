package org.wdt.wdtc.core.game

import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.getString
import java.io.IOException

class GetGameNeedLibraryFile(private val launcher: Launcher) {
  @get:Throws(IOException::class)
  val libraryList: List<LibraryFile>
    get() {
      val fileList: MutableList<LibraryFile> = ArrayList()
      val versionJsonObject = launcher.gameVersionJsonObject
      for (libraryObject in versionJsonObject.libraries!!) {
        if (libraryObject.natives != null) {
          val nativesJson = libraryObject.natives!!
          if (nativesJson.has("windows")) {
            fileList.add(LibraryFile(libraryObject, true))
          }
        } else {
          val rules = libraryObject.rules
          if (rules != null) {
            val actionObject = rules.getJsonObject(rules.size() - 1)
            val action = actionObject.getString("action")
            val osName = actionObject.getJsonObject("os").getString("name")
            if (action == "disallow" && osName == "osx") {
              fileList.add(LibraryFile(libraryObject))
            } else if (action == "allow" && osName == "windows") {
              fileList.add(LibraryFile(libraryObject))
            }
          } else {
            fileList.add(LibraryFile(libraryObject))
          }
        }
      }
      return fileList
    }

  class LibraryFile @JvmOverloads constructor(
    var libraryObject: LibraryObject,
    var nativesLibrary: Boolean = false
  )
}
