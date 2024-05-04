package org.wdt.wdtc.core.openapi.auth

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.openapi.manger.userJson
import org.wdt.wdtc.core.openapi.utils.gson.prettyGsonBuilder
import java.io.File

interface LoginUser {
	suspend fun getUser(): User
}

data class User(
	var userName: String,
	var accessToken: String,
	var type: Accounts.AccountsType,
	var uuid: String,
	var headFile: File
) {
	
	var metaData: String? = null
	
	var base64Data: String? = null
	
	override fun toString(): String {
		return "User(userName=$userName, type=$type, headFile=$headFile)"
	}
}

val isExistUserJsonFile: Boolean
	get() = userJson.isFileExists()

fun setUserToJson(user: User) = userJson.writeObjectToFile(prettyGsonBuilder) { user }

val preferredUser: User
	get() = userJson.readFileToClass()

