package org.wdt.wdtc.core.openapi.utils.gson

import com.google.gson.*
import org.wdt.utils.gson.getString
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toFile
import org.wdt.wdtc.core.openapi.download.interfaces.ModDownloadInfoInterface
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.game.VersionsList
import org.wdt.wdtc.core.openapi.manger.GameFileManger
import java.lang.reflect.Type

class GameVersionsListTypeAdapters : TypeAdapters<VersionsList> {
	override fun serialize(src: VersionsList, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return JsonArray().apply {
			src.forEach {
				add(serializeVersionGson.create().toJsonTree(it))
			}
		}
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): VersionsList {
		require(json.isJsonArray)
		return VersionsList(
			LinkedHashSet(json.asJsonArray.map {
				it.asJsonObject.parseObject<Version>(serializeVersionGson)
			})
		)
	}
}
class GameFileMangerTypeAdapters : TypeAdapters<GameFileManger> {
	override fun serialize(src: GameFileManger, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return JsonObject().apply {
			src.run {
				addProperty("versionNumber", versionNumber)
				addProperty("workDirectory", workDirectory.canonicalPath)
				addProperty("versionDirectory", versionDirectory.canonicalPath)
				addProperty("configFile", versionConfigFile.canonicalPath)
			}
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
			Class.forName("org.wdt.wdtc.core.impl.utils.gson.VersionTypeAdapter").getConstructor().newInstance()
		)

val serializeVersionsListGson: GsonBuilder =
	serializeVersionGson.registerTypeAdapter(VersionsList::class.java, GameVersionsListTypeAdapters())

const val fabricKey = "fabricDownlaodInfo"
const val forgeKey = "forgeDownloadInfo"
const val quiltKey = "quiltDownloadInfo"