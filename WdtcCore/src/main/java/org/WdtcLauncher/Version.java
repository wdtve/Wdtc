package org.WdtcLauncher;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Version {
    private static final File s_j = new File("WdtcCore/ResourceFile/Download/starter.json");
    private static String version;

    public Version(String version) {
        Version.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getVersion_lib_path() throws IOException {
        String s_e = FileUtils.readFileToString(s_j);
        JSONObject s_e_j = JSONObject.parseObject(s_e);
        String v_lib_path = s_e_j.getString("v_lib_path");
        return v_lib_path;
    }

    public String getVersion_path() throws IOException {
        return getVersion_lib_path() + version + "\\";
    }

    public String getVersion_json() throws IOException {
        return getVersion_path() + version + ".json";
    }
}
