package org.wdt.wdtc.core.manger

import com.google.gson.annotations.JsonAdapter
import org.wdt.wdtc.core.utils.gson.SystemKindTypeAdapter

@JsonAdapter(SystemKindTypeAdapter::class)
enum class SystemKind {
	WINDOWS, MAC, LINUX;
	
	companion object {
		val nameToKind = mapOf(
			"windows" to WINDOWS,
			"osx" to MAC,
			"linux" to LINUX
		)
		val kindToName = mapOf(
			WINDOWS to "windows",
			MAC to "osx",
			LINUX to "linux"
		)
	}
}


val currentSystem: SystemKind = if (OS.startsWith("windows", true)) {
	SystemKind.WINDOWS
} else if (OS.startsWith("mac", true)) {
	SystemKind.MAC
} else if (OS.startsWith("linux", true)) {
	SystemKind.LINUX
} else {
	error("Unkown system")
}
val isWindows: Boolean = currentSystem == SystemKind.WINDOWS

val isLinux: Boolean = currentSystem == SystemKind.LINUX

val isMacos: Boolean = currentSystem == SystemKind.MAC
