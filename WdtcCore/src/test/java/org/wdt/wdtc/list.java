package org.wdt.wdtc;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

public class list {
    @Test
    public void ep() {
        File[] file = new File("D:\\PCL2\\.minecraft\\versions\\1.17.1-Fabric 0.13.2\\logs").listFiles();
        for (int i = 1; i < file.length; i++) {
            System.out.println(FileUtils.isFileNewer(file[i], file[i - 1]));
        }
    }
}
