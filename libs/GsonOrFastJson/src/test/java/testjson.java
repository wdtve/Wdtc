import org.junit.jupiter.api.Test;
import org.wdt.platform.gson.JSONObject;

import java.io.IOException;

public class testjson {
    @Test
    public void test() throws IOException {
        String str = "{\"namelist\":{\"name\":456}}";
//        JSONObject jsonObject = Utils.getJSONObject(getClass().getResource("/list.json").getFile());
//        jsonObject.put("name", 45);
        JSONObject jsonObject1 = new JSONObject(str);
        System.out.println(jsonObject1.getJSONObject("namelist").has("naame"));

//        JSONObject.PutKetToFile(getClass().getResource("/list.json").getFile(), jsonObject,"namelist", jsonObject1.toString());
//        System.out.println(jsonObject);

    }
}
