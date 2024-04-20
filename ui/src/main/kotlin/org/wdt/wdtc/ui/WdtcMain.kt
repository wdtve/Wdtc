@file:JvmName("WdtcMain")

package org.wdt.wdtc.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.impl.auth.accounts.yggdrasil.downloadAuthlibInjector
import org.wdt.wdtc.core.impl.download.game.DownloadVersionGameFile.Companion.downloadVersionManifestJsonFileTask
import org.wdt.wdtc.core.impl.manger.ckeckRunEnvironment
import org.wdt.wdtc.core.impl.manger.createNeedDirectories
import org.wdt.wdtc.core.impl.manger.removeConfigDirectory
import org.wdt.wdtc.core.impl.manger.runStartUpTask
import org.wdt.wdtc.core.openapi.auth.printUserList
import org.wdt.wdtc.core.openapi.game.printVersionList
import org.wdt.wdtc.core.openapi.manger.*
import org.wdt.wdtc.core.openapi.utils.isOnline
import org.wdt.wdtc.core.openapi.utils.logmaker
import org.wdt.wdtc.core.openapi.utils.runIfNoNull
import org.wdt.wdtc.ui.window.tryCatching
import javafx.application.Application.launch as launchApp

//@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
fun main(args: Array<String>) {
	val startTime = System.currentTimeMillis()
	tryCatching {
		ckeckRunEnvironment()
		createNeedDirectories()
		if (args.isNotEmpty()) {
			removeConfigDirectory(args.first() == "refresh")
		}
		ckeckJavaFX()
		logmaker.info("===== Wdtc - $launcherVersion =====")
		logmaker.info("Current System:${System.getProperty("os.name")}")
		logmaker.info("Current Platform:$currentSystem")
		logmaker.info("Java Version:${System.getProperty("java.version")}")
		logmaker.info("Java VM Version:${System.getProperty("java.vm.name")}")
		logmaker.info("Java Home:${System.getProperty("java.home")}")
		logmaker.info("Wdtc Debug Mode:$isDebug")
		logmaker.info("Wdtc Application Type:$applicationType")
		logmaker.info("Wdtc Config Path:$wdtcConfig")
		logmaker.info("Setting File:$settingFile")
		logmaker.info("Here:$defaultHere")
		runBlocking(Dispatchers.IO) {
			runStartUpTask()
			launch {
				removePreferredVersion()
				printUserList()
				printVersionList()
			}
			if (isOnline) {
				launch {
					downloadVersionManifestJsonFileTask()
					downloadAuthlibInjector()
				}
			}
//			launch(newSingleThreadContext("Find Java")) { JavaUtils.main(registryKey) }
			launchApp(AppMain::class.java)
		}
		awaitApplicationBreak(startTime)
	}
}

private fun removePreferredVersion() {
	currentSetting.preferredVersion.runIfNoNull {
		if (versionJson.isFileNotExists()) {
			currentSetting.changeSettingToFile {
				preferredVersion = null
			}
		}
	}
}

private fun awaitApplicationBreak(time: Long) {
	logmaker.info("Runtime: ${System.currentTimeMillis() - time}ms")
	logmaker.info("======= Wdtc Stop ========")
}

private val registryKey: Array<String> = arrayOf(
	"HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit",
	"HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Update",
	"HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Web Start Caps",
	"HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JDK"
)

fun ckeckJavaFX() {
	try {
		Class.forName("javafx.application.Application")
	} catch (_: ClassNotFoundException) {
		downloadDependencies()
		loadJavaFXPatch()
	}
}

