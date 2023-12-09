package org.wdt.wdtc.core.utils

import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.isFileExists
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.toFile
import org.wdt.wdtc.core.manger.putSettingToFile
import org.wdt.wdtc.core.manger.setting
import java.io.File
import java.io.IOException
import java.util.regex.Pattern

val runJavaHome: String = JavaUtils.getJavaExePath(File(System.getProperty("java.home")))

object JavaUtils {

  @JvmStatic
  fun main(args: Array<String>) {
    try {
      for (s in args) {
        if (isDownloadProcess) break
        getPotentialJava(s)
      }
      logmaker.info("Find Java Done")
    } catch (e: IOException) {
      logmaker.error("Error, ", e)
    }
  }

  private fun getPotentialJava(key: String) {
    val process = ProcessBuilder("reg", "query", key).start()
    val newJavaList: MutableSet<JavaInfo> = setting.javaPath
    for (s in IOUtils.readLines(process.inputStream)) {
      if (s.startsWith(key)) {
        for (map in getJavaExeAndVersion(getPotentialJavaHome(getPotentialJavaFolders(s)))) {
          if (isDownloadProcess) return
          val newInfo = JavaInfo(File(map["JavaPath"]!!))
          if (newJavaList.add(newInfo)) {
            logmaker.info("Find Java : ${newInfo.javaExeFile}, Version : ${newInfo.versionNumber}")
          }
        }
      }
    }
    setting.javaPath = newJavaList
    putSettingToFile(setting)
  }

  private fun getPotentialJavaFolders(key: String): List<String> {
    val list: MutableList<String> = ArrayList()
    val process = ProcessBuilder("reg", "query", key).start()
    for (s in IOUtils.readLines(process.inputStream)) {
      if (s.startsWith(key)) {
        list.add(s)
      }
    }
    return list
  }

  private fun getPotentialJavaHome(list: List<String>): List<String> {
    val javaHomeList: MutableList<String> = ArrayList()
    val pattern = Pattern.compile("\\s+JavaHome\\s+REG_SZ\\s+(.+)")
    for (key in list) {
      val process = ProcessBuilder("reg", "query", key, "/v", "JavaHome").start()
      for (s in IOUtils.readLines(process.inputStream)) {
        val javaHomeCleaned = s.cleanStrInString(STRING_SPACE)
        if (javaHomeCleaned.startsWith("JavaHome")) {
          val matcher = pattern.matcher(s)
          if (matcher.find()) {
            javaHomeList.add(matcher.group(1))
          }
        }
      }
    }
    return javaHomeList
  }

  private fun getJavaExeAndVersion(list: List<String>): List<Map<String, String?>> {
    val javaList: MutableList<Map<String, String?>> = ArrayList()
    for (path in list) {
      val javaPath = getJavaExePath(File(path))
      if (path.toFile().isFileNotExists()) {
        logmaker.warn("warn : ", IOException("$path isn't exists"))
      } else {
        if (javaPath.toFile().isFileExists()) {
          val javaExeAndVersion: MutableMap<String, String?> = HashMap()
          javaExeAndVersion["JavaPath"] = path
          javaExeAndVersion["JavaVersion"] = getJavaVersion(javaPath)
          javaList.add(javaExeAndVersion)
        }
      }
    }
    return javaList
  }

  fun getJavaVersion(javaPath: String): String? {
    try {
      val process = Runtime.getRuntime().exec(arrayOf(javaPath, "-XshowSettings:properties", "-version"))
      val pattern = Pattern.compile("java\\.version = (.+)")
      for (line in IOUtils.readLines(process.errorStream)) {
        val matcher = pattern.matcher(line)
        if (matcher.find()) {
          return matcher.group(1)
        }
      }
    } catch (e: IOException) {
      logmaker.error(e.getExceptionMessage())
    }
    return null
  }

  fun getJavaExePath(javaHome: File): String {
    val javaHomePath = javaHome.canonicalPath
    return if (Pattern.compile("\\s").matcher(javaHomePath).find()) {
      if (javaHomePath.endsWith("\\")) {
        "\"" + javaHome + "bin\\java.exe\""
      } else {
        "\"$javaHome\\bin\\java.exe\""
      }
    } else {
      if (javaHomePath.endsWith("\\")) {
        javaHome.toString() + "bin\\java.exe"
      } else {
        "$javaHome\\bin\\java.exe"
      }
    }
  }

  fun getJavaExeFile(javaHome: File): File {
    return File(javaHome, "bin/java.exe")
  }

  enum class JavaTips {
    JDK, JRE;

    companion object {
      private fun isJRE(javaHome: File): Boolean = File(javaHome, "bin/javac.exe").isFileNotExists()

      fun getJavaTips(javaHomeFile: File): JavaTips = if (isJRE(javaHomeFile)) JRE else JDK

    }
  }

  data class JavaInfo @JvmOverloads constructor(
    val javaHomeFile: File,
    val javaExeFile: File = getJavaExeFile(javaHomeFile),
    val versionNumber: String? = getJavaVersion(getJavaExePath(javaHomeFile)),
    val tips: JavaTips = JavaTips.getJavaTips(javaHomeFile)
  )
}
