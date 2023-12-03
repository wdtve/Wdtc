package org.wdt.wdtc.core.auth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.utils.gson.Json;
import org.wdt.utils.gson.JsonObjectUtils;
import org.wdt.utils.gson.JsonUtils;
import org.wdt.wdtc.core.manger.FileManger;
import org.wdt.wdtc.core.utils.WdtcLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsersList {
  private static final File usersListFile = FileManger.getUserListFile();
  private static final Logger logmaker = WdtcLogger.getLogger(UsersList.class);

  public static void addUser(User user) throws IOException {
    JsonObject usersList = getUserListObject();
    String userName = user.getUserName();
    if (usersList.has(userName)) {
      logmaker.warn(userName + " Remove");
      usersList.remove(userName);
    }
    usersList.add(userName, Json.GSON.toJsonTree(user, User.class));
    JsonUtils.writeObjectToFile(usersListFile, usersList, Json.getBuilder().setPrettyPrinting());
  }


  public static JsonObject getUserListObject() throws IOException {
    return JsonUtils.readFileToJsonObject(usersListFile);
  }

  public static User getUser(String UserName) throws IOException {
    return JsonObjectUtils.parseObject(getUserListObject().getAsJsonObject(UserName), User.class);
  }

  public static List<User> getUserList() {
    List<User> userList = new ArrayList<>();
    try {
      Map<String, JsonElement> UserListMap = getUserListObject().asMap();
      if (!UserListMap.keySet().isEmpty()) {
        for (String s : UserListMap.keySet()) {
          userList.add(getUser(s));
        }
      }
    } catch (IOException e) {
      logmaker.error(e);
    }
    return userList;
  }

  public static void printUserList() {
    for (User user : getUserList()) {
      logmaker.info(user);
    }
  }
}
