package org.wdt.wdtc.core.openapi.utils.gson

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.wdt.utils.gson.JsonObject
import org.wdt.utils.gson.asJsonArray
import org.wdt.utils.gson.asParseObjectList
import org.wdt.utils.gson.getString
import org.wdt.utils.io.toFile
import org.wdt.wdtc.core.openapi.download.interfaces.ModDownloadInfoInterface
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.game.VersionsList
import org.wdt.wdtc.core.openapi.manger.GameFileManger
import org.wdt.wdtc.core.openapi.plugins.loader.currentClassLoader
import java.lang.reflect.Type

class GameVersionsListTypeAdapters : TypeAdapters<VersionsList> {
	override fun serialize(src: VersionsList, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return src.asJsonArray(serializeVersionGson)
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): VersionsList {
		require(json.isJsonArray)
		return VersionsList(json.asJsonArray.asParseObjectList<Version>(serializeVersionGson).toHashSet())
	}
}

class GameFileMangerTypeAdapters : TypeAdapters<GameFileManger> {
	override fun serialize(src: GameFileManger, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return src.run {
			JsonObject(
				"versionNumber" to versionNumber,
				"workDirectory" to workDirectory.canonicalPath,
				"versionDirectory" to versionDirectory.canonicalPath,
				"configFile" to versionConfigFile.canonicalPath
			)
		}
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): GameFileManger {
		require(json.isJsonObject)
		return json.asJsonObject.run {
			GameFileManger(getString("versionNumber"), getString("workDirectory").toFile())
		}
	}
}

val serializeModDownloadInfoGson: GsonBuilder =
	prettyGsonBuilder.serializeNulls()
		.registerTypeAdapter(ModDownloadInfoInterface::class.java, DownloadInfoTypeAdapter)


val serializeVersionGson: GsonBuilder =
	serializeModDownloadInfoGson.registerTypeAdapter(GameFileManger::class.java, GameFileMangerTypeAdapters())
		.registerTypeAdapter(
			Version::class.java,
			currentClassLoader.loadClass("org.wdt.wdtc.core.impl.utils.gson.VersionTypeAdapter").getConstructor()
				.newInstance()
		)

val serializeVersionsListGson: GsonBuilder =
	serializeVersionGson.registerTypeAdapter(VersionsList::class.java, GameVersionsListTypeAdapters())

const val fabricKey = "fabricDownlaodInfo"
const val forgeKey = "forgeDownloadInfo"
const val quiltKey = "quiltDownloadInfo"