package org.wdt.wdtc.core.utils

import okio.BufferedSource
import okio.FileSystem
import okio.buffer
import okio.source
import org.wdt.utils.io.isFileExists
import org.wdt.utils.io.isFileNotExists
import org.wdt.utils.io.okio.sha1
import org.wdt.utils.io.okio.sizeOf
import org.wdt.wdtc.core.manger.isLowPerformanceMode
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
		require(file.isFileExists()) { "$file must exit" }
		size = file.sizeOf()
		sha1 = if (isLowPerformanceMode) "useless" else file.sha1()
	}
	
	override fun toString(): String {
		return "FileDataImpl(sha1='$sha1', size=$size)"
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
		override val sha1: String = if (isLowPerformanceMode) "useless" else newInputStream().sha1()
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
	require(extension == "jar") { "File must be jar file!" }
	return JarInputStream(inputStream()).manifest.mainAttributes.getValue(key)
}

fun getResourceAsStream(name: String): InputStream {
	return object {}.javaClass.getResourceAsStream(name).noNull()
}

fun File.compareFile(fileData: FileData): Boolean {
	return run {
		if (isFileNotExists()) return true
		compareFileData(toFileData(), fileData)
	}
}

fun compareFileData(fileData1: FileData, fileData2: FileData): Boolean {
	if (fileData1.size != fileData2.size) return true
	if (isLowPerformanceMode) return false
	if (fileData1.sha1 != fileData2.sha1) return true
	return false
}

val fileSystem = FileSystem.SYSTEM