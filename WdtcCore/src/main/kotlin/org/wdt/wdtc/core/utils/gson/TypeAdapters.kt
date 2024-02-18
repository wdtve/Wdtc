package org.wdt.wdtc.core.utils.gson

import com.google.gson.*
import org.wdt.utils.gson.*
import org.wdt.wdtc.core.download.forge.ForgeVersionJsonObject
import org.wdt.wdtc.core.game.GameRuntimeDependency
import org.wdt.wdtc.core.game.LibraryObject
import org.wdt.wdtc.core.game.LibraryObjectList
import org.wdt.wdtc.core.game.Rule.Action
import org.wdt.wdtc.core.game.serializeLibraryObjectGsonBuilder
import org.wdt.wdtc.core.manger.GameDirectoryManger
import org.wdt.wdtc.core.manger.SystemKind
import java.lang.reflect.Type
import java.util.*


val defaultGsonBuilder: GsonBuilder = Json.getBuilder()
val prettyGsonBuilder: GsonBuilder = defaultGsonBuilder.setPrettyPrinting()

interface TypeAdapters<T> : JsonSerializer<T>, JsonDeserializer<T>

class ActionTypeAdapter : TypeAdapters<Action> {
  override fun serialize(src: Action?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
    return if (src == null) {
      JsonNull.INSTANCE
    } else {
      JsonPrimitive(src.name.lowercase(Locale.getDefault()))
    }
  }

  override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Action {
    return if (json.isJsonNull || !json.isJsonPrimitive) {
      throw RuntimeException("json is null or is not primitive")
    } else {
      Action.valueOf(json.asString.uppercase(Locale.getDefault()))
    }
  }
}

class SystemKindTypeAdapter : TypeAdapters<SystemKind> {
  override fun serialize(src: SystemKind?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
    return if (src == null) {
      JsonNull.INSTANCE
    } else {
      val end = SystemKind.kindToName[src]
      if (end == null) {
        JsonNull.INSTANCE
      } else {
        JsonPrimitive(end)
      }
    }
  }

  override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): SystemKind {
    return if (json.isJsonNull || !json.isJsonPrimitive) {
      throw RuntimeException("json is null or is not primitive")
    } else {
      val end = SystemKind.nameToKind[json.asString]
      if (end == null) {
        throw NullPointerException()
      } else {
        end
      }
    }
  }
}

class LibraryObjectListTypeAdapter : TypeAdapters<LibraryObjectList> {
  override fun serialize(src: LibraryObjectList, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
    return JsonArray().apply {
      src.forEach {
        add(serializeLibraryObjectGsonBuilder.create().toJsonTree(it))
      }
    }
  }

  override fun deserialize(
    json: JsonElement,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): LibraryObjectList {
    if (!json.isJsonArray) error("json must be array")
    return LinkedList(json.asJsonArray.map {
      it.asJsonObject.parseObject<LibraryObject>(serializeLibraryObjectGsonBuilder)
    }).let {
      LibraryObjectList(it)
    }
  }
}

class ForgeJsonObjectTypeAdapter : JsonDeserializer<ForgeVersionJsonObject> {
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): ForgeVersionJsonObject {
    if (!json.isJsonObject) error("json must be object")
    return json.asJsonObject.let {
      ForgeVersionJsonObject(
        id = it.getString("id"),
        gameVersionNumber = it.getString("inheritsFrom"),
        mainClass = it.getString("mainClass"),
        arguments = it.getJsonObject("arguments").parseObject(),
        libraries = LibraryObjectList().apply {
          it.getJsonArray("libraries").forEach { library ->
            val libraryObject = library.asJsonObject
            val newDownload = libraryObject.getJsonObject("downloads")
            val artifact = newDownload.getJsonObject("artifact")
            if (artifact.getString("url").isEmpty()) {
              GameRuntimeDependency(libraryObject.getString("name")).apply {
                libraryRepositoriesUrl = GameDirectoryManger().gameLibraryDirectory.toURI().toURL()
              }.run {
                artifact.addProperty("url", libraryUrl.toString())
              }
              newDownload.add("artifact", artifact)
              libraryObject.add("downloads", newDownload)
              add(libraryObject)
            } else {
              add(libraryObject)
            }
          }
        }
      )
    }
  }
}