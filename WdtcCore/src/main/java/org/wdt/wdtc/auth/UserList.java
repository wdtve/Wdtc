package org.wdt.wdtc.auth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.wdt.wdtc.manger.FileManger;
import org.wdt.wdtc.utils.WdtcLogger;
import org.wdt.wdtc.utils.gson.JSON;
import org.wdt.wdtc.utils.gson.JSONObject;
import org.wdt.wdtc.utils.gson.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserList {
    private static final File UserListFile = FileManger.getUserListFile();
    private static final Logger logmaker = WdtcLogger.getLogger(UserList.class);

    public static void addUser(User user) throws IOException {
        JsonObject UserList = UserListObject();
        String UserName = user.getUserName();
        if (UserList.has(UserName)) {
            logmaker.warn("* " + UserName + " Remove");
            UserList.remove(UserName);
        }
        UserList.add(UserName, JSON.GSON.toJsonTree(user, User.class));
        JSONUtils.ObjectToJsonFile(UserListFile, UserList);
    }


    public static JsonObject UserListObject() throws IOException {
        return JSONUtils.getJsonObject(UserListFile);
    }

    public static User getUser(String UserName) throws IOException {
        return JSONObject.parseObject(UserListObject().getAsJsonObject(UserName), User.class);
    }

    public static List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        try {
            JsonObject UserList = UserListObject();
            Map<String, JsonElement> UserListMap = UserList.asMap();
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
