package org.wdt.wdtc.core.openapi.utils

import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.wdt.utils.io.okio.sha1
import org.wdt.utils.io.okio.toBufferedSource
import org.wdt.wdtc.core.openapi.manager.isLowMode
import java.io.Closeable
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.nio.file.Path
import java.util.jar.JarInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

interface FileData {
	val sha1: String?
	val size: Long?
}

internal class FileDataImpl(private val file: File) : FileData {
	override val sha1: String = if (isLowMode) "useless" else file.sha1()
	
	override val size: Long = file.sizeOf()
	
	
	override fun toString(): String {
		return "FileDataImpl(file=$file, sha1='$sha1', size=$size)"
	}
}

fun File.sha1(): String = fileSystem.read(toOkioPath()) {
	readByteString().sha1().hex()
}

fun InputStream.sha1(): String = toBufferedSource().sha1()

fun File.sizeOf(): Long = fileSystem.metadata(toOkioPath()).size.noNull()

fun File.toFileData(): FileData = FileDataImpl(this)

fun String.toFile(): File = File(this)

fun String.toFile(path: String): File = File(this, path)

inline fun <T, I : Closeable, O : Closeable> Pair<I, O>.use(block: (I, O) -> T): T = first.use { input ->
	second.use { output ->
		block(input, output)
	}
}

suspend fun Pair<InputStream, OutputStream>.copyTo() {
	runOnIO {
		use { input, output -> input.copyTo(output) }
	}
}

inline fun <T> File.openZip(block: (ZipFile) -> T): T = ZipFile(this).use(block)


fun URL.toFileData(): FileData = object : FileData {
	override val sha1: String = if (isLowMode) "useless" else newInputStream().sha1()
	override val size: Long = openConnection().contentLengthLong
}

fun ZipEntry.toFileData(zipFile: ZipFile): FileData = object : FileData {
	override val sha1: String = if (isLowMode) "useless"
	else {
		zipFile.getInputStream(this@toFileData).sha1()
	}
	override val size: Long = getSize()
}

fun Path.toFileData() = toFile().toFileData()

fun File.getJarManifestInfo(key: String): String? = JarInputStream(inputStream()).use {
	it.manifest.mainAttributes.getValue(key)
}

fun getResourceAsStream(name: String): InputStream = object {
}.javaClass.getResourceAsStream(name).noNull()

infix fun File.compare(fileData: FileData): Boolean {
	if (!exists()) return true
	return compareFileData(toFileData(), fileData)
}


fun compareFileData(fileData1: FileData, fileData2: FileData): Boolean {
	if (fileData1.size != fileData2.size) return true
	if (isLowMode) return false
	if (fileData1.sha1 != fileData2.sha1) return true
	return false
}

val fileSystem = FileSystem.SYSTEM