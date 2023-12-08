package org.wdt.wdtc.core.utils.dependency

import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class DependencyDownload : DefaultDependency {
  var downloadPath: File? = null
  var defaultUrl = "https://repo1.maven.org/maven2/"

  constructor(lib: String) : super(lib)
  constructor(groupId: String, artifactId: String, version: String) : super(groupId, artifactId, version)

  @get:Throws(IOException::class)
  val libraryFilePath: String
    get() = libraryFile.getCanonicalPath()
  val libraryFile: File
    get() = File(downloadPath, formJar())

  @get:Throws(MalformedURLException::class)
  val libraryUrl: URL
    get() = URL(defaultUrl + formJar())
}