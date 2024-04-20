@file:JvmName("ZipUtils")

package org.wdt.wdtc.core.openapi.utils

import org.wdt.utils.io.*
import java.io.File
import java.util.zip.ZipFile


fun unzipByFile(file: File, path: String) =
	ZipFile(file).use { zip ->
		zip.stream().forEach { entry ->
			if (FilenameUtils.getExtension(entry.name) == "dll") {
				path.toFile(entry.name).run {
					if (compareFile(entry.toFileData(zip))) {
						touch()
						logmaker.info("extract ${entry.name}")
						IOUtils.copy(zip.getInputStream(entry), newOutputStream())
					}
				}
			}
		}
	}


fun unZipBySpecifyFile(zipFile: File, unZipFile: File) = ZipFile(zipFile).use { zip ->
	zip.stream().forEach {
		unZipFile.run {
			if (compareFile(it.toFileData(zip))) {
				touch()
				IOUtils.copy(zip.getInputStream(it), newOutputStream())
			}
		}
	}
}

fun unZipToFile(parentZipFile: File, unFile: File, unFileName: String) {
	ZipFile(parentZipFile).use {
		IOUtils.copy(it.getInputStream(it.getEntry(unFileName)), unFile.outputStream())
	}
}

