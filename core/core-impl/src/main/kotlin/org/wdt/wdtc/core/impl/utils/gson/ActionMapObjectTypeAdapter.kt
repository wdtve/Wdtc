package org.wdt.wdtc.core.impl.utils.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import org.wdt.utils.gson.getString
import org.wdt.wdtc.core.impl.plugins.loader.PluginsLoader
import org.wdt.wdtc.core.impl.plugins.loader.newInstances
import org.wdt.wdtc.core.openapi.plugins.config.ActionMapObject
import org.wdt.wdtc.core.openapi.plugins.interfaces.Action
import org.wdt.wdtc.core.openapi.plugins.interfaces.ActionImpls
import org.wdt.wdtc.core.openapi.utils.gson.TypeAdapters
import org.wdt.wdtc.core.openapi.utils.noNull
import java.lang.reflect.Type

class ActionMapObjectTypeAdapter : TypeAdapters<ActionMapObject> {
	override fun serialize(src: ActionMapObject, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return JsonObject().apply {
			addProperty("action", src.action::class.qualifiedName)
			addProperty("task", src.clazz::class.qualifiedName)
		}
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): ActionMapObject {
		require(json.isJsonObject)
		return json.asJsonObject.run {
			val action = (Class.forName(getString("action")).newInstances() as? Action).noNull()
			val actionImpls = (PluginsLoader.newInstance(getString("task")) as? ActionImpls).noNull()
			ActionMapObject(action, actionImpls)
		}
	}
}