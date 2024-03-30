package org.wdt.wdtc.core.download.infterface

import org.wdt.wdtc.core.game.LibraryObjectList
import java.util.*

interface VersionListInterface {
  val versionList: LinkedList<VersionsJsonObjectInterface>
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