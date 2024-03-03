package org.wdt.wdtc.core.utils

import org.wdt.utils.io.isFileExists
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.readLines
import org.wdt.utils.io.toFile
import org.wdt.wdtc.core.manger.*
import java.io.File
import java.io.IOException
import java.util.regex.Pattern

val runJavaHome: String = JavaUtils.getJavaExePath(File(System.getProperty("java.home")))

object JavaUtils {
	
	@JvmStatic
	fun main(args: Array<String>) {
		try {
			if (!isWindows) {
				logmaker.warning("This utils only can run on windows!")
				return
			}
			if (!isLowPerformanceMode) {
				args.forEach {
					getPotentialJava(it)
				}
			}
			logmaker.info("Find Java Done")
		} catch (e: IOException) {
			logmaker.error("Error, ", e)
		}
	}
	
	private fun getPotentialJava(key: String) {
		val process = ProcessBuilder("reg", "query", key).start()
		val newJavaList: MutableSet<JavaInfo> = currentSetting.javaPath
		process.inputReader().forEachLine {
			if (it.startsWith(key)) {
				getJavaExeAndVersion(getPotentialJavaHome(getPotentialJavaFolders(it))).forEach { map ->
					val newInfo = JavaInfo(File(map["JavaPath"]!!))
					if (newJavaList.add(newInfo)) {
						logmaker.info("Find Java : ${newInfo.javaExeFile}, Version : ${newInfo.versionNumber}")
					}
				}
			}
		}
		currentSetting.changeSettingToFile {
			javaPath = newJavaList
		}
	}
	
	private fun getPotentialJavaFolders(key: String): List<String> {
		val list: MutableList<String> = ArrayList()
		val process = ProcessBuilder("reg", "query", key).start()
		process.inputReader().forEachLine {
			if (it.startsWith(key)) {
				list.add(it)
			}
		}
		return list
	}
	
	private fun getPotentialJavaHome(list: List<String>): List<String> {
		val javaHomeList: MutableList<String> = ArrayList()
		val pattern = Pattern.compile("\\s+JavaHome\\s+REG_SZ\\s+(.+)")
		list.forEach { key ->
			val process = ProcessBuilder("reg", "query", key, "/v", "JavaHome").start()
			process.inputReader().forEachLine {
				if (it.cleanStrInString(STRING_SPACE).startsWith("JavaHome")) {
					pattern.matcher(it).run {
						if (find()) {
							javaHomeList.add(group(1))
						}
					}
				}
			}
		}
		return javaHomeList
	}
	
	private fun getJavaExeAndVersion(list: List<String>): List<Map<String, String?>> {
		val javaList: MutableList<Map<String, String?>> = ArrayList()
		list.forEach { path ->
			val javaPath = getJavaExePath(File(path))
			if (path.toFile().isFileNotExists()) {
				logmaker.warning("$path isn't exists")
			} else {
				if (javaPath.toFile().isFileExists()) {
					HashMap<String, String?>().apply {
						put("JavaPath", path)
						put("JavaVersion", getJavaVersion(javaPath))
					}.let {
						javaList.add(it)
					}
				}
			}
		}
		return javaList
	}
	
	fun getJavaVersion(javaPath: String): String? {
		val process = Runtime.getRuntime().exec(arrayOf(javaPath, "-XshowSettings:properties", "-version"))
		val pattern = Pattern.compile("java\\.version = (.+)")
		for (it in process.errorStream.readLines()) {
			pattern.matcher(it).run {
				if (find())
					return group(1)
			}
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
