package org.wdt.utils.dependency

import org.dom4j.DocumentException
import org.dom4j.io.SAXReader
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class LibraryDependencyDownload : DefaultDependency {
  private var path = System.getProperty("user.dir")
  var defaultUrl = "https://repo1.maven.org/maven2/"
  var isDeletePom = false

  constructor(lib: String) : super(lib)
  constructor(groupId: String, artifactId: String, version: String) : super(groupId, artifactId, version)

  val pomFilePath
    get() = pomFile.canonicalPath


  val pomFile
    get() = File(aboutPath(path), formPom())


  fun getPath(): String {
    return path
  }

  fun setPath(path: String) {
    this.path = aboutPath(path) + "/"
  }

  @get:Throws(MalformedURLException::class)
  val pomUrl: URL
    get() = URL(defaultUrl + formPom().replace("\\+".toRegex(), "%2B"))

  @Throws(DocumentException::class, IOException::class)
  fun downloadDependency(): List<DependencyDownload> {
    if (!pomFile.exists()) {
      throw IOException("The POM file not exists!")
    }
    val dependencyList: MutableList<DependencyDownload> = ArrayList()
    val saxReader = SAXReader()
    val document = saxReader.read(pomFile)
    if (Objects.nonNull(document)) {
      val element = document.rootElement
      val dependencies = element.element("dependencies")
      if (Objects.nonNull(dependencies)) {
        val list = dependencies.elements("dependency")
        for (l in list) {
          if (Objects.isNull(l.elementText("scope"))) {
            dependencyList.add(
              DependencyDownload(
                l.elementText("groupId"),
                l.elementText("artifactId"),
                l.elementText("version")
              )
            )
          } else {
            dependencyList.add(
              DependencyDownload(
                l.elementText("groupId") + ":" + l.elementText("artifactId") +
                    ":" + l.elementText("version") + ":" + l.elementText("scope")
              )
            )
          }
        }
      }
    }
    if (isDeletePom) {
      pomFile.delete()
    }
    return dependencyList
  }

  companion object {
    private fun aboutPath(path: String): String {
      return if (Objects.isNull(path)) "" else path
    }
  }
}
