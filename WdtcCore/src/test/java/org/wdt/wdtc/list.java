package org.wdt.wdtc;


import org.junit.jupiter.api.Test;
import org.wdt.utils.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class list {
    @Test
    public void ep() throws IOException {
        FileUtils.deleteDirectory(new File(".minecraft"));
    }
}
