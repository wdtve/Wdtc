package org.wdt.utils.dependency

import java.util.*
import java.util.regex.Pattern

open class DefaultDependency(libraryName: String) {
  val libraryName: String
  var groupId: String

  @JvmField
  var artifactId: String
  var version: String
  var spaec: String? = null

  init {
    val p = Pattern.compile("([^: ]+):([^: ]+)(:([^: ]*)(:([^: ]+))?)?:([^: ]+)")
    val m = p.matcher(Objects.requireNonNull(libraryName))
    require(m.matches()) { "Bad artifact coordinates $libraryName, expected format is <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>" }
    this.libraryName = libraryName
    groupId = cleanString(m.group(1))
    artifactId = cleanString(m.group(2))
    spaec = if (Objects.nonNull(m.group(3))) {
      cleanString(m.group(3))
    } else {
      null
    }
    version = cleanString(m.group(7))
  }

  constructor(groupId: String, artifactId: String, version: String) : this("$groupId:$artifactId:$version")

  fun formPom(): String {
    return groupId.replace(
      "\\.".toRegex(),
      "/"
    ) + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".pom"
  }

  fun formJar(): String {
    val p = Pattern.compile("@")
    val m = p.matcher(version)
    //net.minecraft:client:1.19.4-20230314.122934:slim net\minecraft\client\1.19.4-20230314.122934\client-1.19.4-20230314.122934-srg.jar
    val artifactid = artifactId.replace("\\.".toRegex(), "-")
    return if (Objects.isNull(spaec)) {
      if (m.find()) {
        (groupId.replace("\\.".toRegex(), "/") + "/" +
            artifactid + "/" + version.substring(0, version.indexOf("@"))
            + "/" + artifactid + "-" + version.replace("@".toRegex(), "."))
      } else {
        (groupId.replace("\\.".toRegex(), "/") + "/"
            + artifactid + "/" + version + "/" + artifactid + "-" + version + ".jar")
      }
    } else {
      if (m.find()) {
        (groupId.replace("\\.".toRegex(), "/") + "/" + artifactId
            + "/" + spaec + "/" + artifactid + "-" + spaec + "-" + version.replace("@".toRegex(), "."))
      } else {
        (groupId.replace("\\.".toRegex(), "/") + "/" + artifactid
            + "/" + spaec + "/" + artifactid + "-" + spaec + "-" + version + ".jar")
      }
    }
  }

  fun getDependencyDownload(): DependencyDownload {
    return DependencyDownload(libraryName)
  }

  open fun getLibraryDependencyDownload(): LibraryDependencyDownload {
    return LibraryDependencyDownload(libraryName)
  }

  fun cleanString(str: String): String {
    return str.replace(":", "").replace("[", "").replace("]", "")
  }
}
