import org.WdtcDownload.SetFilePath.SetPath;

import java.io.File;

public class mkdirs {
    public static void main(String[] args) {
        if (new File(SetPath.getGamePath()).mkdirs()) {
            System.out.println(new File(SetPath.getGamePath()).mkdirs());
        }
    }
}
