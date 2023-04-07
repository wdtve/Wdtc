import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class FabricDownload {
    private static final String version = "1.19.3";
    private static String fabricversion = "0.14.13";
    private static final String fabricurl = "https://meta.fabricmc.net/v2/versions/loader/%s/%s";

    @Test
    public void getfabriclist() throws IOException {
        URL fabric_version_manifest_url = new URL("https://meta.fabricmc.net/v2/versions/loader");
        URLConnection uc = fabric_version_manifest_url.openConnection();
        String inputLine = IOUtils.toString(uc.getInputStream(), StandardCharsets.UTF_8);
        JSONArray fabric_list = JSONArray.parseArray(inputLine);
//        for (int i = 0; i < fabric_list.size(); i++) {
        JSONObject version_name = fabric_list.getJSONObject(1);
        fabricversion = version_name.getString("version");
//        }
        String game = String.format(fabricurl, version, fabricversion);
        URL version_manifest_url = new URL(game);
        URLConnection u_c = version_manifest_url.openConnection();
        String i_nputLine = IOUtils.toString(u_c.getInputStream(), StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File("WdtcCore/src/test/java/list.json"), i_nputLine, "UTF-8");
    }
}
