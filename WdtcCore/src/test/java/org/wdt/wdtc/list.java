package org.wdt.wdtc;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class list {
    @Test
    public void ep() throws IOException {
        FileUtils.deleteDirectory(new File(".minecraft"));
    }
}
