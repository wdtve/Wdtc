package org.wdt.wdtc.core.download.infterface

interface VersionJsonObjectInterface {
  val versionNumber: String?
  override fun hashCode(): Int
  override fun equals(other: Any?): Boolean
}
