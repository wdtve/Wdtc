package org.wdt;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class list {
    @Test
    public void ep() {
        String str = "fabric-loader-0.14.21-1.19.4";

        Pattern r = Pattern.compile("(.+)-(.+)-(.+)");
        Matcher m = r.matcher(str);
        if (m.find()) {
            System.out.println(m.group(1));
        }
    }
}
