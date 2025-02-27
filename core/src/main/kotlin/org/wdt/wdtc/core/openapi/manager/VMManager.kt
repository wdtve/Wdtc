package org.wdt.wdtc.core.openapi.manager

import org.wdt.wdtc.core.openapi.utils.toFile
import java.io.File
import java.lang.Boolean.getBoolean

const val LAUNCHER_VERSION = "wdtc.launcher.version"
const val CONFIG_PATH = "wdtc.config.path"
const val DEBUG = "wdtc.debug.mode"
const val APPLICATION_TYPE = "wtdc.application.type"
const val CLIENT_ID = "wtdc.oauth.clientId"
const val NETWORK_TIMEOUT_TIME = "wdtc.net.timeout"
const val LOW_PERFORMANCE_MODE = "wdtc.low.performance.mode"

// TODO More and more people
private val LAUNCHER_AUTHOR = listOf("Wdt~")

val OS: String = System.getProperty("os.name")

val clientId: String = System.getProperty(CLIENT_ID, "8c4a5ce9-55b9-442e-9bd0-17cf89689dd0")

val launcherVersion: String = System.getProperty(LAUNCHER_VERSION, "demo")

val isDebug: Boolean = getBoolean(DEBUG)


val applicationType: String = System.getProperty(APPLICATION_TYPE, "ui")

val isConsole: Boolean = applicationType == "console"

val isUI: Boolean = applicationType == "ui"

val networkLinkTimeoutTime: Int = System.getProperty(NETWORK_TIMEOUT_TIME, "5000").toInt()

val isLowMode = getBoolean(LOW_PERFORMANCE_MODE)

internal val wdtcConfigFromVM: File = run {
	if (isDebug) System.setProperty(CONFIG_PATH, "./")
	if (System.getProperty(CONFIG_PATH) == null) {
		System.setProperty(CONFIG_PATH, System.getProperty("user.home"))
	}
	System.getProperty(CONFIG_PATH).let {
		it?.toFile()?.normalize() ?: System.getProperty("user.home").toFile().normalize()
	}
}