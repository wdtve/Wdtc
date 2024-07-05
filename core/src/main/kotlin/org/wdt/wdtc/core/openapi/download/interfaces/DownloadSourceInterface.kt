package org.wdt.wdtc.core.openapi.download.interfaces

interface DownloadSourceInterface {
	val libraryURL: String
	val assetsURL: String
	val metaURL: String
	val dataURL: String
	val versionManifestURL: String
	val fabricMetaURL: String
	val fabricLibraryURL: String
	val forgeLibraryMavenURL: String
	val versionClientURL: String
	val officialURL: String
	val quiltMetaURL: String
	val quiltMavenURL: String
	
	companion object {
		const val MOJANG_VERSION_MANIFEST = "https://piston-meta.mojang.com/mc/game/version_manifest.json"
		const val MOJANG_ASSETS = "https://resources.download.minecraft.net/"
		const val MOJANG_LIBRARIES = "https://libraries.minecraft.net/"
		const val PISTON_META_MOJANG = "https://piston-meta.mojang.com/"
		const val PISTON_DATA_MOJANG = "https://piston-data.mojang.com/"
	}
}
