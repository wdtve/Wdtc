import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.wdt.AboutSetting;
import org.wdt.Version;
import org.wdt.WdtcDownload.Fabric.VersionJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class test {
    private static final Logger logmaker = Logger.getLogger(test.class);

    static void function(String filename, File dir) {
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    function(filename, file.getAbsoluteFile());
                }
                if (file.isFile() && filename.equals(file.getName())) {
                    logmaker.info("* " + file.getAbsolutePath());
                    break;
                }
            }
        }
    }

    @Test
    public void urlio() throws IOException {
        Version version = new Version("1.19.4");
        VersionJson versionJson = new VersionJson("0.14.17", version);
        versionJson.modify();
    }

    @Test
    public void here() throws IOException {
        Version version = new Version("1.19.4");
        JSONObject jsonObject = JSONObject.parseObject(FileUtils.readFileToString(new File(version.getVersionJson())));
        JSONArray jsonArray = jsonObject.getJSONArray("libraries");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            System.out.println(jsonObject1);

        }
    }

    @Test
    public void maker() throws IOException {
        Files.createDirectories(Paths.get("hello"));
    }

    @Test
    public void commonio() {
//        File file = new File("F:\\");
//        function("java.exe", file);
        System.out.println(FilenameUtils.getExtension("hello.dll"));
    }

    @Test
    public void isnull() throws IOException {
        AboutSetting.GenerateSettingFile();
    }
}
