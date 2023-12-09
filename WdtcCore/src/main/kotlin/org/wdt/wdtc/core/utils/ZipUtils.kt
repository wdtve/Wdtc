@file:JvmName("ZipUtils")

package org.wdt.wdtc.core.utils

import org.wdt.utils.io.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Pattern
import java.util.zip.ZipFile


fun unzipByFile(file: File, path: String) {
  try {
    val zip = ZipFile(file)
    for (entry in zip.stream().toList()) {
      val name = entry.name
      if (FilenameUtils.getExtension(name) == "dll") {
        val unfile = File(path + File.separator + name)
        if (unfile.isFileNotExists()) {
          logmaker.info("提取natives库dll文件${name}中")
          Files.createFile(Paths.get(path + File.separator + name))
          val input = zip.getInputStream(entry)
          val fos = FileOutputStream(unfile)
          IOUtils.copy(input, fos)
        }
      }
    }
    zip.close()
  } catch (e: Exception) {
    logmaker.error(e.getExceptionMessage())
  }
}

fun unZipBySpecifyFile(zipFile: File, unZipPath: File) {
  try {
    val zip = ZipFile(zipFile)
    for (entry in zip.stream().toList()) {
      val name = entry.name
      if (Pattern.compile(unZipPath.getName()).matcher(name).find()) {
        logmaker.info("提取$unZipPath 中")
        unZipPath.touch()
        val input = zip.getInputStream(entry)
        val fos = unZipPath.newOutputStream()
        IOUtils.copy(input, fos)
      }
    }
    zip.close()
  } catch (e: Exception) {
    logmaker.error(e.getExceptionMessage())
  }
}

fun unZipToFile(parentZipFile: File, unFile: File, unFileName: String) {
  try {
    val zipFile = ZipFile(parentZipFile)
    val outputStream = FileOutputStream(unFile)
    val stream = zipFile.getInputStream(zipFile.getEntry(unFileName))
    IOUtils.copy(stream, outputStream)
  } catch (e: IOException) {
    logmaker.error(e.getExceptionMessage())
  }
}

