package org.wdt;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Pack {
    @Test
    public void exec() {
        try {
            Process process = Runtime.getRuntime().exec("cmd.exe /c C:\\Users\\yuwen\\.wdtc\\WdtcGameLauncherScript.bat");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while (bufferedReader.readLine() != null) {
                System.out.println(IOUtils.toString(bufferedReader));
            }
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
