package org.wdt.WdtcUI.users;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.wdt.FilePath;

import java.io.File;
import java.io.IOException;

public class Registeruser {
    public static void RegisterUser(String users_name) throws IOException {
        Users users = new Users();
        users.setUser_name(users_name);
        String users_string = JSONObject.toJSONString(users);
        File users_json = FilePath.getUsersJson();
        FileUtils.writeStringToFile(users_json, users_string, "UTF-8");
    }
}
