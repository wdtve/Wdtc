@file:JvmName("WdtcMain")

package org.wdt.wdtc.ui

import kotlinx.coroutines.*
import org.wdt.utils.io.isFileNotExists
import org.wdt.wdtc.core.auth.printUserList
import org.wdt.wdtc.core.auth.yggdrasil.updateAuthlibInjector
import org.wdt.wdtc.core.game.printVersionList
import org.wdt.wdtc.core.manger.*
import org.wdt.wdtc.core.utils.JavaUtils
import org.wdt.wdtc.core.utils.isOnline
import org.wdt.wdtc.core.utils.logmaker
import org.wdt.wdtc.core.utils.runIfNoNull
import org.wdt.wdtc.ui.window.setErrorWin
import java.util.*
import javafx.application.Application.launch as launchApp

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
fun main(args: Array<String>) {
	val startTime = System.currentTimeMillis()
	try {
		ckeckRunEnvironment()
		createNeedDirectories()
		if (args.isNotEmpty()) {
			removeConfigDirectory(Arrays.stream(args).toList()[0] == "refresh")
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
					updateAuthlibInjector()
				}
			}
			launch(newSingleThreadContext("Find Java")) { JavaUtils.main(registryKey) }
			launchApp(AppMain::class.java)
		}
		awaitApplicationBreak(startTime)
	} catch (e: Throwable) {
		setErrorWin(e)
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

