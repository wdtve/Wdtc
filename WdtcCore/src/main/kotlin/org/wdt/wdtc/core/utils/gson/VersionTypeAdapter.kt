package org.wdt.wdtc.core.utils.gson

import com.google.gson.*
import org.wdt.utils.gson.checkKey
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.getString
import org.wdt.utils.gson.parseObject
import org.wdt.utils.io.toFile
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.quilt.QuiltDownloadInfo
import org.wdt.wdtc.core.game.Version
import org.wdt.wdtc.core.game.VersionsList
import org.wdt.wdtc.core.manger.GameFileManger
import java.lang.reflect.Type

class LauncherTypeAdapter : TypeAdapters<Version> {
	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): Version {
		require(json.isJsonObject)
		return json.asJsonObject.run {
			Version(getString("versionNumber"), getString("workDirectory").toFile()).apply {
				if (checkKey(fabricKey)) {
					fabricModInstallInfo =
						FabricDonwloadInfo(this, getJsonObject(fabricKey).getString(versionNumberKey))
				}
				if (checkKey(forgeKey)) {
					forgeModDownloadInfo =
						ForgeDownloadInfo(this, getJsonObject(forgeKey).getString(versionNumberKey))
				}
				if (checkKey(quiltKey)) {
					quiltModDownloadInfo =
						QuiltDownloadInfo(this, getJsonObject(quiltKey).getString(quiltKey))
				}
			}
		}
	}
	
	override fun serialize(src: Version, typeOfSrc: Type, context: JsonSerializationContext?): JsonElement {
		return GameFileMangerTypeAdapters().serialize(src, typeOfSrc, context).asJsonObject.apply {
			src.run {
				addProperty("modKind", kind.name)
				serializeModDownloadInfoGson.create().let {
					add(fabricKey, it.toJsonTree(fabricModInstallInfo))
					add(forgeKey, it.toJsonTree(forgeModDownloadInfo))
					add(quiltKey, it.toJsonTree(quiltModDownloadInfo))
				}
			}
		}
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


private val serializeModDownloadInfoGson: GsonBuilder =
	prettyGsonBuilder.serializeNulls()
		.registerTypeAdapter(ModDownloadInfoInterface::class.java, DownloadInfoTypeAdapter())

val serializeVersionGson: GsonBuilder =
	serializeModDownloadInfoGson.registerTypeAdapter(GameFileManger::class.java, GameFileMangerTypeAdapters())
		.registerTypeAdapter(Version::class.java, LauncherTypeAdapter())

val serializeVersionsListGson: GsonBuilder =
	serializeVersionGson.registerTypeAdapter(VersionsList::class.java, GameVersionsListTypeAdapters())

private const val fabricKey = "fabricDownlaodInfo"
private const val forgeKey = "forgeDownloadInfo"
private const val quiltKey = "quiltDownloadInfo"