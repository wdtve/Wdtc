@file:JvmName("UsersList")

package org.wdt.wdtc.core.auth

import org.wdt.utils.gson.Json
import org.wdt.utils.gson.parseObject
import org.wdt.utils.gson.readFileToJsonObject
import org.wdt.utils.gson.writeObjectToFile
import org.wdt.wdtc.core.manger.userListFile
import org.wdt.wdtc.core.utils.getExceptionMessage
import org.wdt.wdtc.core.utils.logmaker
import java.io.IOException


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


fun getUser(userName: String): User = userListFile.readFileToJsonObject().getAsJsonObject(userName).parseObject()


val userList: HashSet<User>
  get() {
    val userList = HashSet<User>()
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

fun printUserList() = userList.forEach { logmaker.info(it) }


