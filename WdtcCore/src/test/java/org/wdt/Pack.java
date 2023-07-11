package org.wdt;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

public class Pack {
    @Test
    public void exec() throws IOException {
        URL url = new URL("https://download.mcbbs.net/maven/org/quiltmc/quilt-json5/1.0.4+final/quilt-json5-1.0.4+final.jar");
        url.openStream();
    }

}
