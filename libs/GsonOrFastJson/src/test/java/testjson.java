import org.junit.jupiter.api.Test;
import org.wdt.platform.gson.JSONObject;

import java.io.IOException;

public class testjson {
    @Test
    public void test() throws IOException {
        JSONObject jsonObject = new JSONObject("""
                {
                    "UserName": "Wd_t",
                    "AccessToken": "${auth_access_token}",
                    "Type": "offline"
                }""");
        com.alibaba.fastjson2.JSONObject jsonObject1 = jsonObject.getFastJSONObject();
        System.out.println(jsonObject1.getString("UserName"));

    }
}
