package org.wdt.wdtc.core.openapi.utils.gson

import com.google.gson.*
import org.wdt.utils.gson.*
import org.wdt.wdtc.core.openapi.game.LibraryObjectList
import org.wdt.wdtc.core.openapi.game.Rules.Rule.Action
import org.wdt.wdtc.core.openapi.game.serializeLibraryObjectGsonBuilder
import org.wdt.wdtc.core.openapi.manager.SystemKind
import org.wdt.wdtc.core.openapi.plugins.config.ModuleActionMaps
import org.wdt.wdtc.core.openapi.plugins.config.PluginConfigFileObject
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObject
import org.wdt.wdtc.core.openapi.plugins.config.PluginsObjectSequence
import org.wdt.wdtc.core.openapi.plugins.loader.currentClassLoader
import org.wdt.wdtc.core.openapi.utils.noNull
import java.lang.reflect.Type
import java.util.*


val defaultGsonBuilder: GsonBuilder = Json.getBuilder()
val prettyGsonBuilder: GsonBuilder = defaultGsonBuilder.setPrettyPrinting()

interface TypeAdapters<T> : JsonSerializer<T>, JsonDeserializer<T>

fun <T> loadTypeAdapters(clazz: String): TypeAdapters<T> {
	return currentClassLoader.loadClass(clazz).getDeclaredConstructor().newInstance() as TypeAdapters<T>
}

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
		return json.asJsonArray.parseObjectListTo(LibraryObjectList(), defaultSerializeLibraryObjectGsonBuilder)
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
		return PluginsObjectSequence(json.asJsonArray.parseObjectList<PluginsObject>(defaultGsonBuilder).asSequence())
	}
}

object PluginConfigFileObjectTypeAdapter : TypeAdapters<PluginConfigFileObject> {
	override fun serialize(
		src: PluginConfigFileObject,
		typeOfSrc: Type?,
		context: JsonSerializationContext
	): JsonElement {
		return JsonObject().apply {
			addProperty("name", src.name)
			addProperty("developer", src.developer)
			add("core", src.coreActions.let {
				loadTypeAdapters<ModuleActionMaps>(it.typeAdapter).serialize(it, it::class.java, context)
			})
			add("console", src.consoleActions.let {
				loadTypeAdapters<ModuleActionMaps>(it.typeAdapter).serialize(it, it::class.java, context)
			})
			add("dependencies", context.serialize(src.dependencies))
		}
	}
	
	override fun deserialize(
		json: JsonElement,
		typeOfT: Type?,
		context: JsonDeserializationContext
	): PluginConfigFileObject {
		json.asJsonObject.let {
			return PluginConfigFileObject(
				it.getString("name"), it.getString("developer"),
				it.getJsonObject("core").let { core ->
					loadTypeAdapters<ModuleActionMaps>(core.getString("typeAdapter")).deserialize(
						core,
						ModuleActionMaps::class.java,
						context
					)
				},
				it.getJsonObject("console").let { console ->
					loadTypeAdapters<ModuleActionMaps>(console.getString("typeAdapter")).deserialize(
						console,
						ModuleActionMaps::class.java,
						context
					)
				},
				context.deserialize(it.getJsonArray("dependencies"), LibraryObjectList::class.java)
			)
		}
	}
	
}