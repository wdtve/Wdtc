package org.wdt.gson;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class testgson {
    @Test
    public void test() {
        Map<String, Object> ObjectMap = new HashMap<>();
        ObjectMap.put("bmcl", false);
        ObjectMap.put("log", false);
        ObjectMap.put("llvmpipe-loader", false);
        ObjectMap.put("DefaultGamePath", System.getProperty("user.dir"));
        ObjectMap.put("JavaPath", new ArrayList<>());
        ObjectMap.put("ZH-CN", true);
        Gson gson = new Gson();
        System.out.println(gson.toJson(ObjectMap));


    }
}
