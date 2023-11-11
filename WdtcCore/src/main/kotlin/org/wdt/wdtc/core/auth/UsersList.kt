package org.wdt.wdtc.core.auth

import org.wdt.utils.gson.*
import org.wdt.wdtc.core.manger.FileManger.userListFile
import org.wdt.wdtc.core.utils.WdtcLogger.getExceptionMessage
import org.wdt.wdtc.core.utils.WdtcLogger.getLogger
import java.io.IOException

object UsersList {
    private val logmaker = getLogger(UsersList::class.java)

    @JvmStatic
    @Throws(IOException::class)
    fun addUser(user: User) {
        val userList = userListFile.readFileToJsonObject()
        val userName = user.userName
        if (userList.has(userName)) {
            logmaker.warn("$userName Remove")
            userList.remove(userName)
        }
        userList.add(userName, Json.GSON.toJsonTree(user, User::class.java))
        userListFile.writeObjectToFile(userList)
    }


    @JvmStatic
    fun getUser(userName: String): User {
        return userListFile.readFileToJsonObject().getAsJsonObject(userName).parseObject()
    }

    @JvmStatic
    val userList: List<User>
        get() {
            val userList = ArrayList<User>()
            try {
                val userListMap = userListFile.readFileToJsonObject().asMap()
                if (userListMap.keys.isNotEmpty()) {
                    for (s in userListMap.keys) {
                        userList.add(getUser(s))
                    }
                }
            } catch (e: IOException) {
                logmaker.error(e.getExceptionMessage())
            }
            return userList
        }

    @JvmStatic
    fun printUserList() {
        for (user in userList) {
            logmaker.info(user)
        }
    }
}
