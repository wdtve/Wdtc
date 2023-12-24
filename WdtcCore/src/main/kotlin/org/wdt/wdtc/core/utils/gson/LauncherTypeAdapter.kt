package org.wdt.wdtc.core.utils.gson

import com.google.gson.*
import org.wdt.utils.gson.Json
import org.wdt.utils.gson.checkKey
import org.wdt.utils.gson.getJsonObject
import org.wdt.utils.gson.getString
import org.wdt.utils.io.toFile
import org.wdt.wdtc.core.download.fabric.FabricDonwloadInfo
import org.wdt.wdtc.core.download.forge.ForgeDownloadInfo
import org.wdt.wdtc.core.download.infterface.ModDownloadInfoInterface
import org.wdt.wdtc.core.download.quilt.QuiltDownloadInfo
import org.wdt.wdtc.core.game.Launcher
import java.lang.reflect.Type

class LauncherTypeAdapter : JsonDeserializer<Launcher>, JsonSerializer<Launcher> {
  override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): Launcher {
    if (!json.isJsonObject) throw RuntimeException()
    val jsonObject = json.asJsonObject
    val newLauncher = Launcher(jsonObject.getString("versionNumber"), jsonObject.getString("workDirectory").toFile())
    if (jsonObject.checkKey(fabricKey)) {
      newLauncher.fabricModInstallInfo = FabricDonwloadInfo(newLauncher, jsonObject.getJsonObject(fabricKey).getString(versionNumberKey))
    }
    if (jsonObject.checkKey(forgeKey)) {
      newLauncher.forgeModDownloadInfo = ForgeDownloadInfo(newLauncher, jsonObject.getJsonObject(forgeKey).getString(versionNumberKey))
    }
    if (jsonObject.checkKey(quiltKey)) {
      newLauncher.quiltModDownloadInfo = QuiltDownloadInfo(newLauncher, jsonObject.getJsonObject(quiltKey).getString(quiltKey))
    }
    return newLauncher
  }

  override fun serialize(src: Launcher, typeOfSrc: Type, context: JsonSerializationContext?): JsonElement {
    val newObject = JsonObject()
    newObject.addProperty("versionNumber", src.versionNumber)
    newObject.addProperty("modKind", src.kind.name)
    newObject.add(fabricKey, serializeGson.toJsonTree(src.fabricModInstallInfo))
    newObject.add(forgeKey, serializeGson.toJsonTree(src.forgeModDownloadInfo))
    newObject.add(quiltKey, serializeGson.toJsonTree(src.quiltModDownloadInfo))
    newObject.addProperty("workDirectory", src.workDirectory.canonicalPath)
    newObject.addProperty("versionDirectory", src.versionDirectory.canonicalPath)
    return newObject
  }
}

private val serializeGson: Gson =
  Json.getBuilder().serializeNulls()
    .registerTypeAdapter(ModDownloadInfoInterface::class.java, DownloadInfoTypeAdapter()).create()

val serializeLauncherGson: GsonBuilder =
  serializeGson.newBuilder().setPrettyPrinting().registerTypeAdapter(Launcher::class.java, LauncherTypeAdapter())

private const val fabricKey = "fabricDownlaodInfo"
private const val forgeKey = "forgeDownloadInfo"
private const val quiltKey = "quiltDownloadInfo"