package org.wdt.wdtc.core.utils

import org.wdt.utils.io.IOUtils
import org.wdt.wdtc.core.utils.StringUtils.appendForString
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
    val conn = toURL(path).openConnection() as HttpURLConnection
    conn.instanceFollowRedirects = false
    conn.setConnectTimeout(5000)
    return conn.getHeaderField("Location")
  }

  @JvmStatic
  val isOnline: Boolean
    get() = isNetworkHasThisFile(toURL("https://www.bilibili.com"))

  @JvmStatic
  @Throws(IOException::class)
  fun getURLToString(urlPath: String): String {
    return IOUtils.toString(toURL(urlPath))
  }

  @JvmStatic
  fun openSomething(o: Any) {
    Runtime.getRuntime().exec(arrayOf("cmd.exe", "/c", "start", o.toString()))
    logmaker.info("$o is open")
  }

  @Throws(IOException::class)
  fun newInputStream(url: URL): InputStream {
    val connection = toHttpsURLConnection(url)
    connection.setConnectTimeout(5000)
    connection.setReadTimeout(5000)
    return connection.inputStream
  }

  @Throws(IOException::class)
  fun toHttpsURLConnection(url: URL): HttpsURLConnection {
    return url.openConnection() as HttpsURLConnection
  }

  @JvmStatic
  fun toURL(url: String): URL {
    return URL(url)
  }

  fun toURL(url: String, url1: String): URL {
    return toURL(url.appendForString(url1))
  }

  fun toURL(url: URL, url1: String): URL {
    return toURL(url.toString().appendForString(url1))
  }
}
