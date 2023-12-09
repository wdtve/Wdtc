package org.wdt.wdtc.core.utils

import org.wdt.utils.io.IOUtils
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection


fun URL.isNetworkHasThisFile(): Boolean {
  return try {
    val uc = this.openConnection()
    uc.connectTimeout = 12000
    uc.connect()
    true
  } catch (e: IOException) {
    false
  }
}

@Throws(IOException::class)
fun URL.getRedirectUrl(): String {
  val conn = this.toHttpsURLConnection()
  conn.instanceFollowRedirects = false
  conn.connectTimeout = 5000
  return conn.getHeaderField("Location")
}

val isOnline: Boolean = "https://www.bilibili.com".toURL().isNetworkHasThisFile()

fun String.getURLToString(): String {
  return IOUtils.toString(this.toURL())
}

fun openSomething(o: Any) {
  Runtime.getRuntime().exec(arrayOf("cmd.exe", "/c", "start", o.toString()))
  logmaker.info("$o is open")
}

fun URL.newInputStream(): InputStream {
  val connection = this.toHttpsURLConnection()
  connection.connectTimeout = 5000
  connection.readTimeout = 5000
  return connection.inputStream
}

fun URL.toHttpsURLConnection(): HttpsURLConnection {
  return this.openConnection() as HttpsURLConnection
}

fun String.toURL(): URL {
  return URL(this)
}

