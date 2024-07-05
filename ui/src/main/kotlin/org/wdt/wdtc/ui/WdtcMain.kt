@file:JvmName("WdtcMain")

package org.wdt.wdtc.ui

import kotlinx.coroutines.launch
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.impl.auth.accounts.yggdrasil.downloadAuthlibInjector
import org.wdt.wdtc.core.impl.download.game.DownloadVersionGameFile.Companion.downloadVersionManifestJsonFileTask
import org.wdt.wdtc.core.impl.manger.ckeckRunEnvironment
import org.wdt.wdtc.core.impl.manger.createNeedDirectories
import org.wdt.wdtc.core.impl.manger.removeConfigDirectory
import org.wdt.wdtc.core.impl.manger.runStartUpTask
import org.wdt.wdtc.core.openapi.auth.printUserList
import org.wdt.wdtc.core.openapi.game.printVersionList
import org.wdt.wdtc.core.openapi.manager.*
import org.wdt.wdtc.core.openapi.utils.*
import org.wdt.wdtc.ui.loader.downloadDependencies
import org.wdt.wdtc.ui.loader.loadJavaFXPatch
import org.wdt.wdtc.ui.window.tryCatching
import javafx.application.Application.launch as launchApp

suspend fun main(args: Array<String>) {
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
		logmaker.info("Kotlin Version:${KotlinVersion.CURRENT}")
		logmaker.info("Java Version:${System.getProperty("java.version")}")
		logmaker.info("Java VM Version:${System.getProperty("java.vm.name")}")
		logmaker.info("Java Home:${System.getProperty("java.home")}")
		logmaker.info("Wdtc Debug Mode:$isDebug")
		logmaker.info("Wdtc Application Type:$applicationType")
		logmaker.info("Wdtc Config Path:$wdtcConfig")
		logmaker.info("Setting File:$settingFile")
		logmaker.info("Here:$defaultHere")
		runOnIO {
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
			launch(newSingleThreadContext()) { JavaUtils.main(registryKey) }
			launchApp(AppMain::class.java)
		}
		awaitApplicationBreak(startTime)
	}
}

private fun removePreferredVersion() {
	currentSetting.preferredVersion?.let {
		if (it.versionJson.isFileNotExists()) {
			currentSetting.saveSettingToFile {
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

suspend fun ckeckJavaFX() {
	try {
		Class.forName("javafx.application.Application")
	} catch (_: ClassNotFoundException) {
		downloadDependencies()
		loadJavaFXPatch()
	}
}

