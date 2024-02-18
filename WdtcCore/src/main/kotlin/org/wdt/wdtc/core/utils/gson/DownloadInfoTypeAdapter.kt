package org.wdt.wdtc.core.utils.gson

import com.google.gson.*
import org.wdt.utils.gson.getString
import org.wdt.wdtc.core.download.game.VersionNotFoundException
import org.wdt.wdtc.core.download.infterface.ModInstallTaskInterface
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.utils.KindOfMod
import java.lang.reflect.Type

class DownloadInfoTypeAdapter : TypeAdapters<ModDownloadInfoInterface> {
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext
  ): ModDownloadInfoInterface {
    if (!json.isJsonObject) throw RuntimeException()
    val jsonObject = json.asJsonObject
    val modVersionNumber = jsonObject.getString(versionNumberKey)
    return object : ModDownloadInfoInterface {
      override val modVersion: String
        get() = modVersionNumber
      override val modInstallTask: ModInstallTaskInterface
        get() = throw RuntimeException("modInstallTask default to null")
      override val modKind: KindOfMod
        get() = when (jsonObject.getString("modKind")) {
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
    }
  }

  override fun serialize(
    src: ModDownloadInfoInterface?,
    typeOfSrc: Type,
    context: JsonSerializationContext
  ): JsonElement {
    if (src == null) {
      return JsonNull.INSTANCE
    } else {
      val newObject = JsonObject()
      newObject.addProperty("modKind", src.modKind.name)
      newObject.addProperty(versionNumberKey, src.modVersion)
      return newObject
    }
  }
}

const val versionNumberKey = "modVersionNumber"