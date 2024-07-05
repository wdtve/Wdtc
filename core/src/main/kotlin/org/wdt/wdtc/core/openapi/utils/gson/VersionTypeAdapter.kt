package org.wdt.wdtc.core.openapi.utils.gson

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.wdt.utils.gson.*
import org.wdt.wdtc.core.impl.download.fabric.FabricVersionImpl
import org.wdt.wdtc.core.impl.download.fabric.FabricVersionImplTypeAdapter
import org.wdt.wdtc.core.impl.download.forge.ForgeVersionImpl
import org.wdt.wdtc.core.impl.download.quilt.QuiltVersionImpl
import org.wdt.wdtc.core.openapi.download.game.VersionNotFoundException
import org.wdt.wdtc.core.openapi.download.interfaces.ModDownloadInfoInterface
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.game.VersionImpl
import org.wdt.wdtc.core.openapi.game.VersionsList
import org.wdt.wdtc.core.openapi.manager.KindOfMod
import org.wdt.wdtc.core.openapi.manager.KindOfMod.*
import org.wdt.wdtc.core.openapi.utils.toFile
import java.lang.reflect.Type

class VersionsListTypeAdapters : TypeAdapters<VersionsList> {
	override fun serialize(src: VersionsList, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return src.asJsonArray(serializeVersionGson)
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): VersionsList {
		return json.asJsonArray.parseObjectListTo(VersionsList(), serializeVersionGson)
	}
}


val serializeModDownloadInfoGson: GsonBuilder =
	prettyGsonBuilder.serializeNulls().registerTypeAdapter(ModDownloadInfoInterface::class.java, DownloadInfoTypeAdapter)


val serializeVersionGson: GsonBuilder = run {
	serializeModDownloadInfoGson.registerTypeAdapter(Version::class.java, VersionTypeAdapter())
}
val serializeVersionsListGson: GsonBuilder = run {
	serializeVersionGson.registerTypeAdapter(VersionsList::class.java, VersionsListTypeAdapters())
}

class VersionTypeAdapter : TypeAdapters<Version> {
	private val adapter = BassVersionTypeAdapter
	override fun serialize(src: Version, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {
		return when (src) {
			is VersionImpl -> adapter.serialize(src, typeOfSrc, context)
			is ForgeVersionImpl -> adapter.serialize(src, typeOfSrc, context)
			is QuiltVersionImpl -> adapter.serialize(src, typeOfSrc, context)
			is FabricVersionImpl -> FabricVersionImplTypeAdapter.serialize(src, typeOfSrc, context)
			else -> {
				throw VersionNotFoundException("Can not be other")
			}
		}
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): Version {
		val modVersion = json.asJsonObject.getString("modVersion")
		return when (json.asJsonObject.get("modKind").parseObject<KindOfMod>()) {
			ORIGINAL -> adapter.deserialize(json, typeOfT, context)
			FABRIC -> FabricVersionImplTypeAdapter.deserialize(json, typeOfT, context)
			FORGE -> ForgeVersionImpl(adapter.deserialize(json, typeOfT, context), modVersion)
			QUILT -> QuiltVersionImpl(adapter.deserialize(json, typeOfT, context), modVersion)
			FABRICAPI -> throw VersionNotFoundException("Can not be this")
			OTHER -> throw VersionNotFoundException("Can not be this")
		}
	}
}

object BassVersionTypeAdapter : TypeAdapters<Version> {
	override fun serialize(src: Version, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return JsonObject(
			"versionNumber" to src.versionNumber,
			"modVersion" to src.modVersion,
			"workDirectory" to src.workDirectory.canonicalPath,
			"versionDirectory" to src.versionDirectory.canonicalPath,
			"configFile" to src.versionConfigFile.canonicalPath,
			"modKind" to src.kind.name
		)
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Version {
		return json.asJsonObject.run {
			VersionImpl(getString("versionNumber"), getString("workDirectory").toFile())
		}
	}
	
}