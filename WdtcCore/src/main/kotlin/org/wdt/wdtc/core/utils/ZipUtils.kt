@file:JvmName("ZipUtils")

package org.wdt.wdtc.core.utils

import org.wdt.utils.io.*
import java.io.File
import java.io.IOException
import java.util.zip.ZipFile


fun unzipByFile(file: File, path: String) {
  val zip = ZipFile(file)
  try {
    zip.stream().forEach {
      if (FilenameUtils.getExtension(it.name) == "dll") {
        path.toFile(it.name).run {
          if (compareFile(it.toFileData(zip))) {
            touch()
            logmaker.info("extract ${it.name}")
            IOUtils.copy(zip.getInputStream(it), newOutputStream())
          }
        }
      }
    }
    zip.close()
  } catch (e: Exception) {
    logmaker.error(e.getExceptionMessage())
    zip.close()
  }
}

fun unZipBySpecifyFile(zipFile: File, unZipFile: File) {
  val zip = ZipFile(zipFile)
  try {
    zip.stream().forEach {
      unZipFile.run {
        if (compareFile(it.toFileData(zip))) {
          touch()
          IOUtils.copy(zip.getInputStream(it), newOutputStream())
        }
      }
    }
    zip.close()
  } catch (e: Exception) {
    logmaker.error(e.getExceptionMessage())
    zip.close()
  }
}

fun unZipToFile(parentZipFile: File, unFile: File, unFileName: String) {
  val zipFile = ZipFile(parentZipFile)
  try {
    val stream = zipFile.getInputStream(zipFile.getEntry(unFileName))
    IOUtils.copy(stream, unFile.outputStream())
    zipFile.close()
  } catch (e: IOException) {
    logmaker.error(e.getExceptionMessage())
    zipFile.close()
  }
}

