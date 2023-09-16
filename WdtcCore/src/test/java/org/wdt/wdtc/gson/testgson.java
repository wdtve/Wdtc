package org.wdt.wdtc.gson;

import org.junit.jupiter.api.Test;
import org.wdt.utils.gson.JSON;

import java.io.File;

public class testgson {
    @Test
    public void test() {
        Demo demo = new Demo();
        demo.setFile(new File("asd.json"));
        System.out.println(JSON.FILE_GSON.toJson(demo));
    }

    public static class Demo {
        public File file;

        public void setFile(File file) {
            this.file = file;
        }
    }
}
