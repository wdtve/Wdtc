package org.wdt.wdtc.core.utils

import org.wdt.wdtc.core.manger.networkimeoutTime
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection


fun URL.isNetworkHasThisFile(): Boolean {
	return try {
		this.openConnection().run {
			connectTimeout = networkimeoutTime
			connect()
		}
		true
	} catch (e: IOException) {
		false
	}
}

@Throws(IOException::class)
fun URL.getRedirectUrl(): String {
	return this.toHttpsURLConnection().run {
		instanceFollowRedirects = false
		connectTimeout = networkimeoutTime
		getHeaderField("Location")
	}
}

val isOnline: Boolean = "https://www.bilibili.com".toURL().isNetworkHasThisFile()


fun openSomething(o: Any) {
	o.toString().let {
		scwn(it) {
			Runtime.getRuntime().exec(arrayOf("cmd.exe", "/c", "start", it))
			logmaker.info("$it is open")
		}
	}
}

fun URL.newInputStream(): InputStream {
	return this.toHttpsURLConnection().run {
		connectTimeout = networkimeoutTime
		readTimeout = networkimeoutTime
		inputStream
	}
}

fun URL.toHttpsURLConnection(): HttpsURLConnection {
	return this.openConnection() as HttpsURLConnection
}

fun String.toURL(): URL {
	return URL(this)
}

