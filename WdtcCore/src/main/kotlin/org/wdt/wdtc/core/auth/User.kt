package org.wdt.wdtc.core.auth

import org.wdt.utils.gson.Json
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.FileManger.userJson
import java.io.File
import java.util.*

class User @JvmOverloads constructor(
  var userName: String? = null,

  var accessToken: String? = null,

  var type: AccountsType? = null,

  var uuid: String? = null,

  var metaData: String? = null,

  var base64Data: String? = null,

  var headFile: File? = null
) {


  override fun hashCode(): Int {
    return Objects.hash(userName, accessToken, type, uuid, metaData)
  }

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

    return true
  }

  companion object {
    @JvmStatic
    fun setUserToJson(user: User) {
      userJson.writeObjectToFile(user, Json.GSON_BUILDER.setPrettyPrinting())
    }

    @JvmStatic
    val user: User = userJson.readFileToClass()

    @JvmStatic
    val isExistUserJsonFile: Boolean = userJson.isFileExists()
  }
}
