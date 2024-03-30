package org.wdt.wdtc.core.auth

import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.userJson
import org.wdt.wdtc.core.utils.gson.prettyGsonBuilder
import java.io.File

interface LoginUser {
	val user: User
}

data class User(
	var userName: String,
	var accessToken: String,
	var type: AccountsType,
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

fun setUserToJson(user: User) = userJson.writeObjectToFile(user, prettyGsonBuilder)

val preferredUser: User
	get() = userJson.readFileToClass()

