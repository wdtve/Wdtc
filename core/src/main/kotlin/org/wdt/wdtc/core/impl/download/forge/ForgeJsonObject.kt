package org.wdt.wdtc.core.impl.download.forge

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.getInt
import org.wdt.utils.gson.getString
import org.wdt.wdtc.core.openapi.download.interfaces.VersionJsonObjectInterface
import org.wdt.wdtc.core.openapi.game.Arguments
import org.wdt.wdtc.core.openapi.game.LibraryObject.Artifact
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.game.serializeLibraryObjectGsonBuilder
import org.wdt.wdtc.core.openapi.manager.GameDirectoryManager
import org.wdt.wdtc.core.openapi.utils.gson.LibraryObjectListTypeAdapter
import org.wdt.wdtc.core.openapi.utils.startendWith
import org.wdt.wdtc.core.openapi.utils.toURL
import java.lang.reflect.Type

val forgeJsonObjectGsonBuilder: GsonBuilder = serializeLibraryObjectGsonBuilder.registerTypeAdapter(
	Artifact::class.java,
	ForgeArtifactTypeAdapter
).let {
	it.registerTypeAdapter(LibraryObjectList::class.java, LibraryObjectListTypeAdapter(it))
}


class ForgeVersionJsonObject(
	@field:SerializedName("id")
	override var id: String,
	
	@field:SerializedName("mainClass")
	var mainClass: String,
	
	@field:SerializedName("inheritsFrom")
	var gameVersionNumber: String,
	
	@field:SerializedName("arguments")
	var arguments: Arguments,
	
	@field:SerializedName("libraries")
	override var libraries: LibraryObjectList,
) : VersionJsonObjectInterface


class ForgeInstallProfileJsonObject(
	
	@field:SerializedName("version")
	override var id: String,
	
	@field:SerializedName("minecraft")
	val gameVersionNumber: String,
	
	@field:SerializedName("data")
	val data: ForgeDataMaps,
	
	@field:SerializedName("processors")
	val processors: List<ForgeProcessorObject>,
	
	@field:SerializedName("libraries")
	override var libraries: LibraryObjectList,
) : VersionJsonObjectInterface {
	
	class ForgeDataObject(
		@field:SerializedName("client")
		val client: String,
		@field:SerializedName("server")
		val server: String
	) {
		val isRuntime: Boolean
			get() = client.startendWith("[", "]")
	}
	
	class ForgeDataMaps {
		@SerializedName("MAPPINGS")
		val mappings: ForgeDataObject? = null
		
		@SerializedName("MOJMAPS")
		val mojmaps: ForgeDataObject? = null
		
		@SerializedName("MERGED_MAPPINGS")
		val mergedMappings: ForgeDataObject? = null
		
		@SerializedName("BINPATCH")
		val binpatch: ForgeDataObject? = null
		
		@SerializedName("MC_SLIM")
		val mcSlim: ForgeDataObject? = null
		
		@SerializedName("MC_SLIM_SHA")
		val mcSlimSha: ForgeDataObject? = null
		
		@SerializedName("MC_EXTRA")
		val mcExtra: ForgeDataObject? = null
		
		@SerializedName("MC_EXTRA_SHA")
		val mcExtraSha: ForgeDataObject? = null
		
		@SerializedName("MC_SRG")
		val mcSrg: ForgeDataObject? = null
		
		@SerializedName("PATCHED")
		val patched: ForgeDataObject? = null
		
		@SerializedName("PATCHED_SHA")
		val patchedSha: ForgeDataObject? = null
		
		@SerializedName("MCP_VERSION")
		val mcpVersion: ForgeDataObject? = null
	}
	
	class ForgeProcessorObject(
		@field:SerializedName("jar")
		val libraryName: String,
		
		@field:SerializedName("classpath")
		val classpath: List<String>,
		
		@field:SerializedName("args")
		val args: List<String>,
		
		) {
		@SerializedName("sides")
		val sides: List<String>? = null
		
		@SerializedName("outputs")
		val outputs: JsonObject? = null
	}
	
}

object ForgeArtifactTypeAdapter : JsonDeserializer<Artifact> {
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): Artifact {
		if (!json.isJsonObject) error("json must be object")
		return json.asJsonObject.run {
			Artifact(
				path = getString("path"),
				sha1 = getString("sha1"),
				size = getInt("size").toLong(),
				url = getString("url").let {
					if (it.isEmpty())
						GameDirectoryManager.DEFAULT_GAME_DIRECTORY.gameLibraryDirectory.resolve(getString("path")).toURI().toURL()
					else it.toURL()
				}
			)
		}
	}
	
}
