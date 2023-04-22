import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class testjson {
    @Test
    public void testJson() throws IOException {
        File file = new File("E:\\Wdtc\\WdtcCore\\src\\test\\java\\jasd.json");
        JSONObject a = JSONObject.parseObject(FileUtils.readFileToString(file, "UTF-8"));
        JSONArray b = a.getJSONArray("name");
        b.add(13);
        String s = JSON.toJSONString(b);
        System.out.println(s);
    }
}
