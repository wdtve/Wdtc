package org.wdt.wdtc.core.utils

import org.wdt.utils.io.FileUtils
import org.wdt.utils.io.IOUtils
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
        val newJavaList: MutableList<JavaInfo> = ArrayList()
        for (s in IOUtils.readLines(process.inputStream)) {
            if (s.startsWith(key)) {
                for (map in getJavaExeAndVersion(getPotentialJavaHome(getPotentialJavaFolders(s)))) {
                    val NewInfo = JavaInfo(File(map["JavaPath"]))
                    var AddPath = true
                    for (info in newJavaList) {
                        if (AddPath) {
                            if (NewInfo == info) {
                                AddPath = false
                            }
                        }
                    }
                    if (AddPath) {
                        newJavaList.add(NewInfo)
                        logmaker.info("Find Java : ${NewInfo.javaExeFile}, Version : ${NewInfo.versionNumber}")
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
        val JavaList: MutableList<Map<String, String?>> = ArrayList()
        for (path in list) {
            val JavaPath = getJavaExePath(File(path))
            if (!Files.exists(Paths.get(path))) {
                logmaker!!.warn("warn : ", IOException("$path isn't exists"))
            } else {
                if (FileUtils.isFileExists(FileUtils.toFile(JavaPath))) {
                    val javaExeAndVersion: MutableMap<String, String?> = HashMap()
                    javaExeAndVersion["JavaPath"] = path
                    javaExeAndVersion["JavaVersion"] = getJavaVersion(JavaPath)
                    JavaList.add(javaExeAndVersion)
                }
            }
        }
        return JavaList
    }

    @JvmStatic
    fun getJavaVersion(JavaPath: String): String? {
        try {
            val process = Runtime.getRuntime().exec(arrayOf(JavaPath, "-XshowSettings:properties", "-version"))
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

    fun getJavaExePath(JavaHome: File): String {
        val JavaHomePath = FileUtils.getCanonicalPath(JavaHome)
        return if (Pattern.compile("\\s").matcher(JavaHomePath).find()) {
            if (JavaHomePath.endsWith("\\")) {
                "\"" + JavaHome + "bin\\java.exe\""
            } else {
                "\"$JavaHome\\bin\\java.exe\""
            }
        } else {
            if (JavaHomePath.endsWith("\\")) {
                JavaHome.toString() + "bin\\java.exe"
            } else {
                "$JavaHome\\bin\\java.exe"
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
                    return FileUtils.isFileNotExists(File(javaHome, "bin/javac.exe"))
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
    )
}
