import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class FabricDownload {
    private static final String version = "1.19.3";
    private static String fabricversion = "0.14.14";
    private static String fabricurl = "https://meta.fabricmc.net/v2/versions/loader/:game_version/:loader_version";

    public static void main(String[] args) throws IOException {
        StringBuilder json = new StringBuilder();
        URL fabric_version_manifest_url = new URL("https://meta.fabricmc.net/v2/versions/loader");
        URLConnection uc = fabric_version_manifest_url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
        String inputLine = "";
        while ((inputLine = in.readLine()) != null) {
            json.append(inputLine);
        }
        JSONArray fabric_list = JSONArray.parseArray(json.toString());
//        for (int i = 0; i < fabric_list.size(); i++) {
            JSONObject version_name = fabric_list.getJSONObject(1);
            fabricversion = version_name.getString("version");
//        }
        StringBuilder s_b = new StringBuilder();
        String game = fabricurl.replaceAll(":game_version",version);
        game = game.replaceAll(":loader_version", fabricversion);
        URL version_manifest_url = new URL(game);
        URLConnection u_c = version_manifest_url.openConnection();
        BufferedReader i_n = new BufferedReader(new InputStreamReader(u_c.getInputStream(), StandardCharsets.UTF_8));
        String i_nputLine = "";
        while ((i_nputLine = i_n.readLine()) != null) {
            s_b.append(i_nputLine);
        }
        FileUtils.writeStringToFile(new File("WdtcCore/src/test/java/list.json"),s_b.toString());
    }
}
