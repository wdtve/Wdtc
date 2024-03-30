package org.wdt.wdtc.core.utils

import org.wdt.wdtc.core.manger.networkimeoutTime
import java.io.IOException
import java.io.InputStream
import java.net.URL


fun URL.isNetworkHasThisFile(): Boolean = try {
	this.openConnection().run {
		connectTimeout = networkimeoutTime
		connect()
	}
	true
} catch (e: IOException) {
	false
}

fun URL.getRedirectUrl(): String = this.openConnection().apply {
	connectTimeout = networkimeoutTime
}.getHeaderField("Location")

val isOnline: Boolean
	get() = "https://www.bilibili.com".toURL().isNetworkHasThisFile()


fun openSomething(o: Any) {
	o.toString().also {
		launchScope(it) {
			Runtime.getRuntime().exec(arrayOf("cmd.exe", "/c", "start", it))
			logmaker.info("$it is open")
		}
	}
}

fun URL.newInputStream(): InputStream = openConnection().apply {
	connectTimeout = networkimeoutTime
	readTimeout = networkimeoutTime
}.inputStream

fun String.toURL(): URL {
	return URL(this)
}

