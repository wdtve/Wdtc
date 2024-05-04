package org.wdt.wdtc.core.openapi.utils.gson

import com.google.gson.*
import org.wdt.utils.gson.Json
import org.wdt.utils.gson.asJsonArray
import org.wdt.utils.gson.asParseObjectList
import org.wdt.wdtc.core.openapi.game.LibraryObject
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.game.LibraryObjectList.Companion.asLibraryObjectList
import org.wdt.wdtc.core.openapi.game.Rules.Rule.Action
import org.wdt.wdtc.core.openapi.game.serializeLibraryObjectGsonBuilder
import org.wdt.wdtc.core.openapi.manger.SystemKind
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObject
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObjectSequence
import org.wdt.wdtc.core.openapi.utils.noNull
import java.lang.reflect.Type
import java.util.*


val defaultGsonBuilder: GsonBuilder = Json.getBuilder()
val prettyGsonBuilder: GsonBuilder = defaultGsonBuilder.setPrettyPrinting()

interface TypeAdapters<T> : JsonSerializer<T>, JsonDeserializer<T>

object ActionTypeAdapter : TypeAdapters<Action> {
	override fun serialize(src: Action?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return if (src == null) {
			JsonNull.INSTANCE
		} else {
			JsonPrimitive(src.name.lowercase(Locale.getDefault()))
		}
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Action {
		require(json.isJsonPrimitive)
		return Action.valueOf(json.asString.uppercase(Locale.getDefault()))
	}
}

object SystemKindTypeAdapter : TypeAdapters<SystemKind> {
	override fun serialize(src: SystemKind?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return if (src == null) {
			JsonNull.INSTANCE
		} else {
			SystemKind.kindToName[src].let {
				if (it == null) {
					JsonNull.INSTANCE
				} else {
					JsonPrimitive(it)
				}
			}
		}
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): SystemKind {
		return if (json.isJsonNull || !json.isJsonPrimitive) {
			throw RuntimeException("json is null or is not primitive")
		} else {
			SystemKind.nameToKind[json.asString].noNull()
		}
	}
}

class LibraryObjectListTypeAdapter(
	private val defaultSerializeLibraryObjectGsonBuilder: GsonBuilder = serializeLibraryObjectGsonBuilder
) : TypeAdapters<LibraryObjectList> {
	override fun serialize(src: LibraryObjectList, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return src.asJsonArray(defaultSerializeLibraryObjectGsonBuilder)
	}
	
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): LibraryObjectList {
		if (!json.isJsonArray) error("json must be array")
		return json.asJsonArray
			.asParseObjectList<LibraryObject>(defaultSerializeLibraryObjectGsonBuilder)
			.asLibraryObjectList()
	}
}

object PluginsObjectSequenceTypeAdapter : TypeAdapters<PluginsObjectSequence> {
	override fun serialize(
		src: PluginsObjectSequence,
		typeOfSrc: Type?,
		context: JsonSerializationContext?
	): JsonElement {
		return src.toList().asJsonArray(defaultGsonBuilder)
	}
	
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): PluginsObjectSequence {
		return PluginsObjectSequence(json.asJsonArray.asParseObjectList<PluginsObject>(defaultGsonBuilder).asSequence())
	}
	
}