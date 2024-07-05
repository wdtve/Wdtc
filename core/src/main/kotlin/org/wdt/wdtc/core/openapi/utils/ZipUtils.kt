@file:JvmName("ZipUtils")

package org.wdt.wdtc.core.openapi.utils

import java.io.File
import kotlin.streams.asSequence


suspend fun unzipByFile(file: File, path: String) = file.openZip { zip ->
	zip.stream().asSequence().filter {
		it.name.substringAfterLast('.', "") == "dll"
	}.associateWith {
		path.toFile(it.name).apply { createNewFile() }
	}.forEach { (entry, file) ->
		if (file compare entry.toFileData(zip)) {
			(zip.getInputStream(entry) to file.outputStream()).copyTo()
		}
	}
}


suspend fun unZipBySpecifyFile(zipFile: File, unZipFile: File) = zipFile.openZip { zip ->
	zip.stream().asSequence().forEach {
		if (unZipFile compare it.toFileData(zip)) {
			(zip.getInputStream(it) to unZipFile.apply {
				createNewFile()
			}.outputStream()).copyTo()
		}
	}
}

suspend fun unZipToFile(parentZipFile: File, unFile: File, unFileName: String) = parentZipFile.openZip {
	runOnIO {
		(it.getInputStream(it.getEntry(unFileName)) to unFile.outputStream()).copyTo()
		
	}
}

