package org.wdt.wdtc.gson;

import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.wdt.utils.io.FileUtils;
import org.wdt.wdtc.core.utils.gson.JSON;
import org.wdt.wdtc.core.utils.gson.JSONObject;

import java.io.File;

public class JsonTest {
    @Test
    public void test() {
        Demo demo = new Demo();
        demo.setFile(FileUtils.toFile("./out.txt"));
        Demo1 demo1 = new Demo1();
        demo1.setObject(new JSONObject(JSON.GSON.toJsonTree(demo1, Demo1.class).getAsJsonObject()));
        System.out.println(JSON.FILE_GSON.toJson(demo1));
    }

    @Setter
    public static class Demo {
        public File file;

    }

    @Setter
    public static class Demo1 {
        public JSONObject object;

    }
}
