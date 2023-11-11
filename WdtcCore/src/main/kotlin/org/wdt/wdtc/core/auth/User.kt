package org.wdt.wdtc.core.auth

import com.google.gson.annotations.SerializedName
import org.wdt.utils.gson.readFileToClass
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.utils.io.isFileExists
import org.wdt.wdtc.core.auth.accounts.Accounts.AccountsType
import org.wdt.wdtc.core.manger.FileManger.userJson
import java.io.File

class User {
    @SerializedName("UserName")
    var userName: String? = null

    @SerializedName("AccessToken")
    var accessToken: String? = null

    @SerializedName("Type")
    var type: AccountsType? = null

    @SerializedName("Uuid")
    var uuid: String? = null

    @SerializedName("MetaAPI")
    var metaData: String? = null

    @SerializedName("MetaAPIBase64")
    var base64Data: String? = null

    @SerializedName("HeadPhotoPath")
    var headFile: File? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other !is User) false else userName == other.userName
    }


    override fun hashCode(): Int {
        var result = userName?.hashCode() ?: 0
        result = 31 * result + (accessToken?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (uuid?.hashCode() ?: 0)
        result = 31 * result + (metaData?.hashCode() ?: 0)
        result = 31 * result + (base64Data?.hashCode() ?: 0)
        result = 31 * result + (headFile?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(userName=$userName, type=$type, headFile=$headFile)"
    }

    companion object {
        @JvmStatic
        fun setUserToJson(user: User) {
            userJson.writeObjectToFile(user)
        }

        @JvmStatic
        val user: User
            get() = userJson.readFileToClass()

        @JvmStatic
        val isExistUserJsonFile: Boolean
            get() = userJson.isFileExists()
    }
}
