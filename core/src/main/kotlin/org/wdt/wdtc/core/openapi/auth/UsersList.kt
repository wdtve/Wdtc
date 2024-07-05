package org.wdt.wdtc.core.openapi.auth

import com.google.gson.*
import org.wdt.utils.gson.asJsonArray
import org.wdt.utils.gson.parseObjectListTo
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.openapi.auth.UsersList.Companion.saveChangeToFile
import org.wdt.wdtc.core.openapi.manager.userJson
import org.wdt.wdtc.core.openapi.manager.userListFile
import org.wdt.wdtc.core.openapi.utils.gson.TypeAdapters
import org.wdt.wdtc.core.openapi.utils.gson.prettyGsonBuilder
import org.wdt.wdtc.core.openapi.utils.info
import org.wdt.wdtc.core.openapi.utils.logmaker
import java.lang.reflect.Type


class UsersList(
	private val usersList: LinkedHashSet<User> = linkedSetOf()
) : MutableSet<User> by usersList {
	
	suspend fun add(loginUser: LoginUser) {
		userJson.writeObjectToFile(prettyGsonBuilder) {
			loginUser.getUser().also {
				logmaker.info(it)
				usersList.add(it)
			}
		}
	}
	
	override fun remove(element: User): Boolean {
		preferredUser.let {
			if (it == element) {
				userJson.writeObjectToFile { JsonObject() }
			}
		}
		return usersList.remove(element)
	}
	
	suspend fun LoginUser.addToList() {
		add(this)
	}
	
	companion object {
		fun UsersList.saveChangeToFile() {
			userListFile.writeObjectToFile(serializeUsersListGsonBuilder) { this }
		}
	}
}

class UsersListTypeAdapters : TypeAdapters<UsersList> {
	override fun serialize(src: UsersList, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return src.asJsonArray()
	}
	
	override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): UsersList {
		return json.asJsonArray.parseObjectListTo(UsersList())
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