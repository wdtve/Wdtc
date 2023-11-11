package org.wdt.wdtc.core.download.infterface

interface VersionJsonObjectInterface {
    val versionNumber: String?
    fun isInstanceofThis(o: Any?): Boolean
}
