package org.wdt.wdtc.core.openapi.utils

import org.wdt.wdtc.core.openapi.manager.currentSetting
import org.wdt.wdtc.core.openapi.manager.isLowMode
import org.wdt.wdtc.core.openapi.manager.isWindows
import org.wdt.wdtc.core.openapi.manager.saveSettingToFile
import java.io.File
import java.io.IOException
import java.util.regex.Pattern

val runJavaHome: String = JavaUtils.getJavaExePath(File(System.getProperty("java.home")))

object JavaUtils {
	
	@JvmStatic
	suspend fun main(args: Array<String>) {
//		TODO Has bug
		try {
			if (!isWindows) {
				logmaker.warning("This utils only can run on windows!")
				return
			}
			if (!isLowMode) {
				currentSetting.saveSettingToFile {
					javas = args.getPotentialJava()
				}
			}
			logmaker.info("Find Java Done")
		} catch (e: IOException) {
			logmaker.error("Error, ", e)
		}
	}
	
	private suspend fun Array<String>.getPotentialJava() = map { key ->
		val process = runOnIO {
			ProcessBuilder("reg", "query", key).start()
		}
		if (process.waitFor() == 1) {
			emptySet()
		} else {
			process.inputStream.reader().useLines { sequence ->
				sequence.filter {
					it.startsWith(key)
				}.map {
					it.getPotentialJavaFolders().getPotentialJavaHome().getJavaExeAndVersion()
				}.flatten().map {
					JavaInfo(it["JavaPath"]!!.toFile())
				}.distinct().toSet()
			}
		}
	}.flatten().toSet()
	
	
	private fun String.getPotentialJavaFolders(): Sequence<String> =
		ProcessBuilder("reg", "query", this).start().inputStream.reader().useLines { sequence ->
			sequence.filter {
				it.startsWith(this)
			}
		}
	
	private fun Sequence<String>.getPotentialJavaHome(): Sequence<String> {
		return map { key ->
			run {
				ProcessBuilder("reg", "query", key, "/v", "JavaHome").start()
			}.inputStream.reader().useLines { sequence ->
				sequence.filter {
					it.cleanStrInString(STRING_SPACE).startsWith("JavaHome")
				}.map {
					Pattern.compile("\\s+JavaHome\\s+REG_SZ\\s+(.+)").matcher(it)
				}.filter {
					it.find()
				}.map {
					it.group(1)
				}
			}
		}.flatten()
	}
	
	private fun Sequence<String>.getJavaExeAndVersion(): List<HashMap<String, String>> {
		return filter {
			it.toFile().exists()
		}.associateWith {
			getJavaExePath(it.toFile())
		}.map { (path, javapath) ->
			hashMapOf(
				"JavaPath" to path, "JavaVersion" to getJavaVersion(javapath)
			)
		}
	}
	
	fun getJavaVersion(javaPath: String): String {
		return run {
			Runtime.getRuntime().exec(arrayOf(javaPath, "-XshowSettings:properties", "-version"))
		}.errorStream.bufferedReader().useLines {
			it.map { path ->
				Pattern.compile("java\\.version = (.+)").matcher(path)
			}.filter { matcher ->
				matcher.find()
			}.first().group(1)
		}
	}
	
	fun getJavaExePath(javaHome: File): String {
		return javaHome.canonicalPath.let {
			if (Pattern.compile("\\s").matcher(it).find()) {
				if (it.endsWith("\\")) {
					"\"" + javaHome + "bin\\java.exe\""
				} else {
					"\"$javaHome\\bin\\java.exe\""
				}
			} else {
				if (it.endsWith("\\")) {
					javaHome.toString() + "bin\\java.exe"
				} else {
					"$javaHome\\bin\\java.exe"
				}
			}
		}
	}
	
	fun getJavaExeFile(javaHome: File): File {
		return File(javaHome, "bin/java.exe")
	}
	
	enum class JavaTips {
		JDK, JRE;
		
		companion object {
			private fun isJRE(javaHome: File): Boolean = !File(javaHome, "bin/javac.exe").exists()
			
			fun getJavaTips(javaHomeFile: File): JavaTips = if (isJRE(javaHomeFile)) JRE else JDK
			
		}
	}
	
	data class JavaInfo(
		val javaHomeFile: File,
		val javaExeFile: File = getJavaExeFile(javaHomeFile),
		val versionNumber: String? = getJavaVersion(getJavaExePath(javaHomeFile)),
		val tips: JavaTips = JavaTips.getJavaTips(javaHomeFile)
	)
}
