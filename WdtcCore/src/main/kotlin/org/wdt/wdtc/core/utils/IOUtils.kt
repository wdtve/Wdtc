package org.wdt.wdtc.core.utils

import okio.BufferedSource
import okio.FileSystem
import okio.buffer
import okio.source
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.okio.sha1
import org.wdt.utils.io.okio.sizeOf
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.file.Path
import java.util.jar.JarInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

interface FileData {
  val sha1: String
  val size: Long
}

class FileDataImpl(file: File) : FileData {
  override val sha1: String
  override val size: Long

  init {
    if (file.isFileNotExists()) error("$file must exit")
    sha1 = file.sha1()
    size = file.sizeOf()
  }
}

fun InputStream.sha1(): String {
  return toBufferedSource().readByteString().sha1().hex()
}

fun InputStream.toBufferedSource(): BufferedSource {
  return source().buffer()
}

fun File.toFileData(): FileData = FileDataImpl(this)

fun URL.toFileData(): FileData {
  return object : FileData {
    override val sha1: String = newInputStream().sha1()
    override val size: Long = openConnection().contentLengthLong
  }
}

fun ZipEntry.toFileData(zipFile: ZipFile): FileData {
  return object : FileData {
    override val sha1: String = zipFile.getInputStream(this@toFileData).sha1()
    override val size: Long = getSize()
  }
}

fun Path.toFileData() = toFile().toFileData()

fun File.getJarManifestInfo(key: String): String? {
  if (this.extension != "jar") error("File must be jar file!")
  return JarInputStream(inputStream()).manifest.mainAttributes.getValue(key)
}

fun getResourceAsStream(name: String): InputStream {
  return object {}.javaClass.getResourceAsStream(name).ckeckIsNull()
}

fun File.compareFile(fileData: FileData): Boolean {
  return run {
    if (isFileNotExists()) return true
    compareFileData(toFileData(), fileData)
  }
}

fun compareFileData(fileData1: FileData, fileData2: FileData): Boolean {
  if (fileData1.size != fileData2.size) return true
  if (fileData1.sha1 != fileData2.sha1) return true
  return false
}

val fileSystem = FileSystem.SYSTEM