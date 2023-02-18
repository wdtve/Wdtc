package org.WdtcDownload;

import javafx.scene.control.TextField;
import org.WdtcDownload.SetFilePath.SetPath;
import org.WdtcDownload.VDownload.SelectVersion;
import org.WdtcLauncher.Version;

import java.io.IOException;

public class DownloadMain {
    public static void main(String version_number, TextField label, boolean BMCLAPI) throws IOException, InterruptedException {
        SetPath.main();
        Version version = new Version(version_number);
        String v_path = version.getVersion_path();
        new SelectVersion(version_number, v_path, label, BMCLAPI).select_v();
    }
}