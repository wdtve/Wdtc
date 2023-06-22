package org.wdt.test;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.wdt.Launcher;
import org.wdt.Version;
import org.wdt.download.dependency.DependencyDownload;
import org.wdt.download.fabric.VersionJson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    private static final Logger logmaker = Logger.getLogger(test.class);

    static void function(File dir) {
        File[] files = dir.listFiles();
        if (Objects.nonNull(files)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    function(file.getAbsoluteFile());
                }
                if (file.isFile() && "java.exe".equals(file.getName())) {
                    File file1 = new File(file.getParent());
                    try {
                        List<String> allLines = Files.readAllLines(Paths.get(file1.getParent() + "/release"));
                        for (String line : allLines) {
                            Pattern p = Pattern.compile("JAVA_VERSION=");
                            Matcher m = p.matcher(line);
                            if (m.find()) {
                                logmaker.info("* 已找到Java:" + file.getAbsolutePath() + "版本:" + line.replace("JAVA_VERSION=", "").replace("\"", ""));
                            }
                        }
                    } catch (IOException e) {
                        logmaker.info("* 已找到Java:" + file.getAbsolutePath());
                    }
                    break;
                }
            }
        }
    }

    public static void executeCmdCommand(String cmdCommand) {
        try {
            Process process = Runtime.getRuntime().exec(cmdCommand);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.err.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public void commonio() throws URISyntaxException {
//        File file = new File("F:\\");
//        function("java.exe", file);
        System.out.println(new File(test.class.getResource("/jasd.json").toURI()));
    }

    @Test
    public void isnull() throws IOException {
        DependencyDownload download = new DependencyDownload("[de.oceanlabs.mcp:mcp_config:1.19.4-20230314.122934:mappings@txt]");
        Launcher launcher = new Launcher("1.19.4");
        download.setPath(launcher.GetGameLibraryPath());
        System.out.println(download.libFilePath());
    }


}
