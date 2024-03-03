package org.wdt.wdtc.core.auth

import com.google.gson.*
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.auth.UsersList.Companion.saveChangeToFile
import org.wdt.wdtc.core.manger.userJson
import org.wdt.wdtc.core.manger.userListFile
import org.wdt.wdtc.core.utils.gson.TypeAdapters
import org.wdt.wdtc.core.utils.gson.defaultGsonBuilder
import org.wdt.wdtc.core.utils.gson.prettyGsonBuilder
import org.wdt.wdtc.core.utils.info
import org.wdt.wdtc.core.utils.logmaker
import java.lang.reflect.Type


class UsersList(
	private val usersList: LinkedHashSet<User> = linkedSetOf()
) : MutableSet<User> by usersList {
	fun add(loginUser: LoginUser) {
		loginUser.user.let {
			userJson.writeObjectToFile(it, prettyGsonBuilder)
			logmaker.info(it)
			this.add(it)
		}
	}
	
	override fun remove(element: User): Boolean {
		preferredUser.let {
			if (it == element) {
				userJson.writeObjectToFile(JsonObject())
			}
		}
		return usersList.remove(element)
	}
	
	fun LoginUser.addToList() {
		add(this)
	}
	
	companion object {
		fun UsersList.saveChangeToFile() {
			userListFile.writeObjectToFile(this, serializeUsersListGsonBuilder)
		}
	}
}

class UsersListTypeAdapters : TypeAdapters<UsersList> {
	override fun serialize(src: UsersList, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return JsonArray().apply {
			src.forEach {
				add(defaultGsonBuilder.create().toJsonTree(it))
			}
		}
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): UsersList {
		if (!json.isJsonArray) error("json must be array")
		return LinkedHashSet(json.asJsonArray.map {
			it.asJsonObject.parseObject<User>()
		}).let {
			UsersList(it)
		}
	}
	
}

val serializeUsersListGsonBuilder: GsonBuilder =
	prettyGsonBuilder.registerTypeAdapter(UsersList::class.java, UsersListTypeAdapters())

fun printUserList() = currentUsersList.forEach { logmaker.info(it) }


val currentUsersList: UsersList
	get() = userListFile.readFileToClass(serializeUsersListGsonBuilder)

inline fun UsersList.changeListToFile(block: UsersList.() -> Unit): UsersList {
	return apply(block).also { it.saveChangeToFile() }
}