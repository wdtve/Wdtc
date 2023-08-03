package org.wdt.wdtc.download;

import org.apache.log4j.Logger;
import org.wdt.wdtc.utils.WdtcLogger;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadUtils {
    private static final Logger logmaker = WdtcLogger.getLogger(DownloadUtils.class);

    public static void DownloadFile(File DownloadFile, URL SrcousFileUrl) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) SrcousFileUrl.openConnection();
        FileOutputStream DownloadFileOutput = new FileOutputStream(DownloadFile);
        InputStream UrlFileInput = connection.getInputStream();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
//        double InitialFileSize = 0;
        double Donwloaded;
        Thread.currentThread().setName(DownloadFile.getName());
        byte[] data = new byte[1024];
        while ((Donwloaded = UrlFileInput.read(data, 0, 1024)) >= 0) {
//            InitialFileSize += Donwloaded;
            DownloadFileOutput.write(data, 0, (int) Donwloaded);
        }
        DownloadFileOutput.close();
        UrlFileInput.close();
    }

    public static void ManyTimesToTryDownload(File DownloadFile, URL SrcousFileUrl, int times) {
        IOException exception = null;
        for (int i = 0; i < times; i++) {
            try {
                DownloadFile(DownloadFile, SrcousFileUrl);
                return;
            } catch (IOException e) {
                exception = e;
            }
        }
        if (exception != null) {
            logmaker.warn("* Thread " + Thread.currentThread().getName() + " Error,", exception);
        }
    }
}
