package org.wdt.wdtc.core.utils

import org.wdt.utils.io.IOUtils
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object URLUtils {
  private val logmaker = WdtcLogger.getLogger(URLUtils::class.java)

  @JvmStatic
  fun isNetworkHasThisFile(url: URL): Boolean {
    return try {
      val uc = url.openConnection()
      uc.setConnectTimeout(12000)
      uc.connect()
      true
    } catch (e: IOException) {
      false
    }
  }

  @JvmStatic
  @Throws(IOException::class)
  fun getRedirectUrl(path: String): String {
    val conn = path.toURL().openConnection() as HttpURLConnection
    conn.instanceFollowRedirects = false
    conn.setConnectTimeout(5000)
    return conn.getHeaderField("Location")
  }

  @JvmStatic
  val isOnline: Boolean
    get() = isNetworkHasThisFile("https://www.bilibili.com".toURL())

  @Throws(IOException::class)
  fun String.getURLToString(): String {
    return IOUtils.toString(this.toURL())
  }

  @JvmStatic
  fun openSomething(o: Any) {
    Runtime.getRuntime().exec(arrayOf("cmd.exe", "/c", "start", o.toString()))
    logmaker.info("$o is open")
  }

  @Throws(IOException::class)
  fun URL.newInputStream(): InputStream {
    val connection = this.toHttpsURLConnection()
    connection.setConnectTimeout(5000)
    connection.setReadTimeout(5000)
    return connection.inputStream
  }

  @Throws(IOException::class)
  fun URL.toHttpsURLConnection(): HttpsURLConnection {
    return this.openConnection() as HttpsURLConnection
  }

  fun String.toURL(): URL {
    return URL(this)
  }
}
