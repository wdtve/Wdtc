package org.wdt.wdtc.core.openapi.utils.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import org.wdt.utils.gson.getString
import org.wdt.wdtc.core.openapi.download.game.VersionNotFoundException
import org.wdt.wdtc.core.openapi.download.interfaces.ModDownloadInfoInterface
import org.wdt.wdtc.core.openapi.download.interfaces.ModInstallTaskInterface
import org.wdt.wdtc.core.openapi.manger.KindOfMod
import java.lang.reflect.Type

object DownloadInfoTypeAdapter : TypeAdapters<ModDownloadInfoInterface> {
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type,
		context: JsonDeserializationContext
	): ModDownloadInfoInterface {
		require(json.isJsonObject)
		return json.asJsonObject.let {
			object : ModDownloadInfoInterface {
				override val modVersion: String
					get() = it.getString(versionNumberKey)
				override val modInstallTask: ModInstallTaskInterface
					get() = throw RuntimeException("modInstallTask default to null")
				override val modKind: KindOfMod
					get() = when (it.getString("modKind")) {
						"FABRIC" -> KindOfMod.FABRIC
						"FABRICAPI" -> throw VersionNotFoundException("The type cannot be FABRICAPI")
						"FORGE" -> KindOfMod.FORGE
						"QUILT" -> KindOfMod.QUILT
						else -> {
							throw VersionNotFoundException("The type cannot be other")
						}
					}
				
				override fun toString(): String {
					return "`<no name provided>`(modVersion='$modVersion', modKind=$modKind)"
				}
				
				override fun equals(other: Any?): Boolean {
					return false
				}
				
				override fun hashCode(): Int {
					return 0
				}
			}
		}
	}
	
	override fun serialize(
		src: ModDownloadInfoInterface,
		typeOfSrc: Type,
		context: JsonSerializationContext
	): JsonElement {
		return JsonObject().apply {
			addProperty("modKind", src.modKind.name)
			addProperty(versionNumberKey, src.modVersion)
		}
	}
}

const val versionNumberKey = "modVersionNumber"