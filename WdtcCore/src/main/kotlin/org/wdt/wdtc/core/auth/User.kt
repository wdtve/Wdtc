package org.wdt.wdtc.core.auth

import org.wdt.utils.gson.Json
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.userJson
import java.io.File

class User @JvmOverloads constructor(
  var userName: String? = null,

  var accessToken: String? = null,

  var type: AccountsType? = null,

  var uuid: String? = null,

  var metaData: String? = null,

  var base64Data: String? = null,

  var headFile: File? = null
) {


  override fun toString(): String {
    return "User(userName=$userName, type=$type, headFile=$headFile)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as User

    if (userName != other.userName) return false
    if (accessToken != other.accessToken) return false
    if (type != other.type) return false
    if (uuid != other.uuid) return false
    if (metaData != other.metaData) return false
    if (base64Data != other.base64Data) return false

    return true
  }

  override fun hashCode(): Int {
    var result = userName?.hashCode() ?: 0
    result = 31 * result + (accessToken?.hashCode() ?: 0)
    result = 31 * result + (type?.hashCode() ?: 0)
    result = 31 * result + (uuid?.hashCode() ?: 0)
    result = 31 * result + (metaData?.hashCode() ?: 0)
    result = 31 * result + (base64Data?.hashCode() ?: 0)
    return result
  }

}

fun setUserToJson(user: User) = userJson.writeObjectToFile(user, Json.GSON_BUILDER.setPrettyPrinting())

val preferredUser: User = userJson.readFileToClass()

val isExistUserJsonFile: Boolean = userJson.isFileExists()
