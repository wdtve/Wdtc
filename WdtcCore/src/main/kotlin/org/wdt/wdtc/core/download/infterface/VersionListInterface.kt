package org.wdt.wdtc.core.download.infterface

import org.wdt.wdtc.core.download.game.GameVersionsObjectList
import org.wdt.wdtc.core.game.LibraryObjectList

interface VersionListInterface {
  val versionList: GameVersionsObjectList
}

interface VersionsJsonObjectInterface {
  val versionNumber: String
  override fun hashCode(): Int
  override fun equals(other: Any?): Boolean
}

interface VersionJsonObjectInterface {
  var id: String
  var libraries: LibraryObjectList
}