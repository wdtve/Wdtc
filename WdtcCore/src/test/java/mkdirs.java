import org.wdt.WdtcDownload.GetGamePath;

import java.io.File;

public class mkdirs {
    public static void main(String[] args) {
        if (new File(GetGamePath.getGamePath()).mkdirs()) {
            System.out.println(new File(GetGamePath.getGamePath()).mkdirs());
        }
    }
}
