package org.wdt.wdtc.core.impl.download.quilt

import com.google.gson.annotations.SerializedName
import org.wdt.wdtc.core.openapi.game.Arguments
import org.wdt.wdtc.core.openapi.game.GameRuntimeDependency
import org.wdt.wdtc.core.openapi.game.LibraryObject
import org.wdt.wdtc.core.openapi.manger.currentDownloadSource
import org.wdt.wdtc.core.openapi.utils.toURL
import java.net.URL

class QuiltProfileJsonObject(
	@field:SerializedName("id")
	val id: String,
	@field:SerializedName("arguments")
	val arguments: Arguments,
	@field:SerializedName("mainClass")
	val mainClass: String,
	@field:SerializedName("libraries")
	val libraries: List<QuiltProfileLibraryObject>
)

class QuiltProfileLibraryObject(
	@field:SerializedName("name")
	val libraryName: GameRuntimeDependency,
	@field:SerializedName("url")
	val url: URL
) {
	suspend fun toLibraryObject(): LibraryObject {
		return LibraryObject.getLibraryObject(libraryName.apply {
			libraryRepositoriesUrl = currentDownloadSource.quiltMavenUrl.toURL()
		}, url)
	}
}
