import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;
import org.wdt.StringUtil;

import java.io.File;
import java.io.IOException;

public class testjson {
    @Test
    public void testJson() throws IOException {
        File file = new File("E:\\Wdtc\\WdtcCore\\src\\test\\java\\jasd.json");
        JSONObject a = StringUtil.FileToJSONObject(file);
        String s = "asd";
        System.out.println(JSON.toJSONString(s));
    }
}
