package org.wdt.wdtc.core.utils

import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.IOUtils
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.manger.SettingManger
import org.wdt.wdtc.core.utils.StringUtils.cleanStrInString
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Pattern

object JavaUtils {
  private val logmaker = WdtcLogger.getLogger(JavaUtils::class.java)

  @JvmStatic
  val runJavaHome: String
    get() = getJavaExePath(File(System.getProperty("java.home")))

  @JvmStatic
  fun main(args: Array<String>) {
    try {
      for (s in args) {
        if (DownloadUtils.isDownloadProcess) {
          break
        }
        getPotentialJava(s)
      }
      logmaker.info("Find Java Done")
    } catch (e: IOException) {
      logmaker.error("Error, ", e)
    }
  }

  @Throws(IOException::class)
  private fun getPotentialJava(key: String) {
    val process = ProcessBuilder("reg", "query", key).start()
    val setting = SettingManger.setting
    val newJavaList: MutableList<JavaInfo> = setting.javaPath
    for (s in IOUtils.readLines(process.inputStream)) {
      if (s.startsWith(key)) {
        for (map in getJavaExeAndVersion(getPotentialJavaHome(getPotentialJavaFolders(s)))) {
          if (DownloadUtils.isDownloadProcess) return
          val newInfo = JavaInfo(File(map["JavaPath"]))
          var addList = true
          for (info in newJavaList) {
            if (addList) {
              if (newInfo == info) {
                addList = false
              }
            }
          }
          if (addList) {
            newJavaList.add(newInfo)
            logmaker.info("Find Java : ${newInfo.javaExeFile}, Version : ${newInfo.versionNumber}")
          }
        }
      }
    }
    setting.javaPath = newJavaList
    SettingManger.putSettingToFile(setting)
  }

  @Throws(IOException::class)
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

  @Throws(IOException::class)
  private fun getPotentialJavaHome(list: List<String>): List<String> {
    val javaHomeList: MutableList<String> = ArrayList()
    val pattern = Pattern.compile("\\s+JavaHome\\s+REG_SZ\\s+(.+)")
    for (key in list) {
      val process = ProcessBuilder("reg", "query", key, "/v", "JavaHome").start()
      for (s in IOUtils.readLines(process.inputStream)) {
        val javaHomeCleaned = s.cleanStrInString(StringUtils.STRING_SPACE)
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

  @Throws(IOException::class)
  fun getJavaExeAndVersion(list: List<String>): List<Map<String, String?>> {
    val javaList: MutableList<Map<String, String?>> = ArrayList()
    for (path in list) {
      val javaPath = getJavaExePath(File(path))
      if (!Files.exists(Paths.get(path))) {
        logmaker.warn("warn : ", IOException("$path isn't exists"))
      } else {
        if (FileUtils.isFileExists(FileUtils.toFile(javaPath))) {
          val javaExeAndVersion: MutableMap<String, String?> = HashMap()
          javaExeAndVersion["JavaPath"] = path
          javaExeAndVersion["JavaVersion"] = getJavaVersion(javaPath)
          javaList.add(javaExeAndVersion)
        }
      }
    }
    return javaList
  }

  @JvmStatic
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
    JDK,
    JRE;

    companion object {
      fun isJRE(javaHome: File): Boolean {
        try {
          return File(javaHome, "bin/javac.exe").isFileNotExists()
        } catch (e: IOException) {
          logmaker.error(e.getExceptionMessage())
        }
        return false
      }

      fun getJavaTips(javaHomeFile: File): JavaTips {
        return if (isJRE(javaHomeFile)) JRE else JDK
      }
    }
  }

  class JavaInfo @JvmOverloads constructor(
    val javaHomeFile: File,
    val javaExeFile: File = getJavaExeFile(javaHomeFile),
    val versionNumber: String? = getJavaVersion(getJavaExePath(javaHomeFile)),
    val tips: JavaTips = JavaTips.getJavaTips(javaHomeFile)

  ) {
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as JavaInfo

      if (javaHomeFile != other.javaHomeFile) return false
      if (javaExeFile != other.javaExeFile) return false
      if (versionNumber != other.versionNumber) return false
      if (tips != other.tips) return false

      return true
    }

    override fun hashCode(): Int {
      var result = javaHomeFile.hashCode()
      result = 31 * result + javaExeFile.hashCode()
      result = 31 * result + (versionNumber?.hashCode() ?: 0)
      result = 31 * result + tips.hashCode()
      return result
    }
  }
}
