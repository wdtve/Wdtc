package org.wdt.wdtc.core.openapi.download.interfaces

import com.google.gson.JsonArray
import org.wdt.utils.gson.parseObject
import org.wdt.wdtc.core.openapi.game.LibraryObjectList

interface VersionListInterface {
	suspend fun getVersionList(): VersionsObjectSequence
}

interface VersionsJsonObjectInterface {
	val versionNumber: String
	override fun hashCode(): Int
	override fun equals(other: Any?): Boolean
}

interface VersionJsonObjectInterface {
	var id: String
	var libraries: LibraryObjectList
}

class VersionsObjectSequence(
	private val sequence: Sequence<VersionsJsonObjectInterface> = emptySequence()
) : Sequence<VersionsJsonObjectInterface> by sequence {
	
	
	companion object {
		inline fun <reified T : VersionsJsonObjectInterface> JsonArray.asSequence(): Sequence<T> {
			return iterator().asSequence().map { it.asJsonObject.parseObject<T>() }
		}
		
		fun <T : VersionsJsonObjectInterface> Sequence<T>.asVersionsObjectSequence(): VersionsObjectSequence {
			return VersionsObjectSequence(this)
		}
	}
	
}