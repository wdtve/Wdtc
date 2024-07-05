package org.wdt.wdtc.core.openapi.utils

import org.wdt.wdtc.core.openapi.manager.networkLinkTimeoutTime
import java.io.InputStream
import java.net.SocketTimeoutException
import java.net.URL


fun URL.isNetworkHasThisFile(): Boolean = runCatching {
	openConnection().apply {
		connectTimeout = networkLinkTimeoutTime
	}.connect()
}.isSuccess

fun URL.getRedirectUrl(): String = this.openConnection().apply {
	connectTimeout = networkLinkTimeoutTime
}.getHeaderField("Location")

val isOnline: Boolean
	get() = "https://www.bilibili.com".toURL().isNetworkHasThisFile()


fun openSomething(o: Any) {
	logmaker.info {
		buildString {
			append(o).also {
				launchOnIO { Runtime.getRuntime().exec(arrayOf("cmd.exe", "/c", "start", it.toString())) }
			}.append(" is open")
		}
	}
}

@Throws(SocketTimeoutException::class)
fun URL.newInputStream(): InputStream = openConnection().apply {
	connectTimeout = networkLinkTimeoutTime
	readTimeout = networkLinkTimeoutTime
}.inputStream

fun String.toURL(): URL {
	return URL(this)
}