package org.WdtcDownload.SetFilePath;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SetPath {
    public static void main() throws IOException {
        GamePath game_path = new GamePath();
        String here = System.getProperty("user.dir");
        game_path.setStarter_here(here);

        game_path.setGame_path(here + "\\.minecraft\\");

        game_path.setGame_lib_path(here + "\\.minecraft\\libraries\\");

        game_path.setV_lib_path(here + "\\.minecraft\\versions\\");

        game_path.setGame_assetsDir(here + "\\.minecraft\\assets\\");

        String game_set = JSON.toJSONString(game_path);

        File l_j = new File("WdtcCore/ResourceFile/Download/starter.json");
        FileUtils.writeStringToFile(l_j, game_set);
    }
}
