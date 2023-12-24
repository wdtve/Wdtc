package org.wdt.wdtc.core.auth

import org.wdt.utils.gson.Json
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.userJson
import java.io.File

data class User @JvmOverloads constructor(
  var userName: String,

  var accessToken: String,

  var type: AccountsType,

  var uuid: String,

  var metaData: String? = null,

  var base64Data: String? = null,

  var headFile: File
) {
  override fun toString(): String {
    return "User(userName=$userName, type=$type, headFile=$headFile)"
  }
}

val isExistUserJsonFile: Boolean = userJson.isFileExists()
fun setUserToJson(user: User) = userJson.writeObjectToFile(user, Json.GSON_BUILDER.setPrettyPrinting())

fun getPreferredUser(): User = userJson.readFileToClass()

