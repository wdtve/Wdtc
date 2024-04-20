package org.wdt.wdtc.core.impl.utils.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.wdt.utils.gson.checkKey
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.getString
import org.wdt.utils.io.toFile
import org.wdt.wdtc.core.impl.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.impl.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.impl.download.quilt.QuiltDownloadInfo
import org.wdt.wdtc.core.openapi.download.interfaces.ModDownloadInfoInterface
import org.wdt.wdtc.core.openapi.game.Version
import org.wdt.wdtc.core.openapi.manger.KindOfMod
import org.wdt.wdtc.core.openapi.manger.isFabric
import org.wdt.wdtc.core.openapi.manger.isForge
import org.wdt.wdtc.core.openapi.manger.isQuilt
import org.wdt.wdtc.core.openapi.utils.gson.*
import java.lang.reflect.Type

class VersionTypeAdapter : TypeAdapters<Version> {
	override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): Version {
		require(json.isJsonObject)
		return json.asJsonObject.run {
			Version(getString("versionNumber"), getString("workDirectory").toFile()).apply {
				if (checkKey(fabricKey)) {
					modDownloadInfo = FabricDonwloadInfo(this, getJsonObject(fabricKey).getString(versionNumberKey))
				}
				if (checkKey(forgeKey)) {
					modDownloadInfo = ForgeDownloadInfo(this, getJsonObject(forgeKey).getString(versionNumberKey))
				}
				if (checkKey(quiltKey)) {
					modDownloadInfo = QuiltDownloadInfo(this, getJsonObject(quiltKey).getString(versionNumberKey))
				}
			}
		}
	}
	
	override fun serialize(src: Version, typeOfSrc: Type, context: JsonSerializationContext?): JsonElement {
		return GameFileMangerTypeAdapters().serialize(src, typeOfSrc, context).asJsonObject.apply {
			src.run {
				addProperty("modKind", kind.name)
				serializeModDownloadInfoGson.create().let {
					add(fabricKey, it.toJsonTree(getDownloadInfo(KindOfMod.FABRIC)))
					add(forgeKey, it.toJsonTree(getDownloadInfo(KindOfMod.FORGE)))
					add(quiltKey, it.toJsonTree(getDownloadInfo(KindOfMod.QUILT)))
				}
			}
		}
	}
	
	private fun Version.getDownloadInfo(kindOfMod: KindOfMod): ModDownloadInfoInterface? = when (kindOfMod) {
		KindOfMod.ORIGINAL -> null
		KindOfMod.FABRIC -> if (isFabric) modDownloadInfo else null
		KindOfMod.FABRICAPI -> null
		KindOfMod.FORGE -> if (isForge) modDownloadInfo else null
		KindOfMod.QUILT -> if (isQuilt) modDownloadInfo else null
	}
}

