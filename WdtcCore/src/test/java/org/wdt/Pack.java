package org.wdt;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pack {
    @Test
    public void exec() throws IOException {
        String s = "[de.oceanlabs.mcp:mcp_config:1.20.1-20230612.114412@zip]";
        Matcher matcher = getMiddleBracket(s);
        Matcher m = getLargeBracket(s);
        if (matcher.find()) {
            System.out.println(s.replaceAll("[a-zA-z]", ""));
        } else if (m.find()) {
            System.out.println(m.group(1));

        }
    }

    public Matcher getMiddleBracket(String args) {
        return Pattern.compile("\\[(.+)]").matcher(args);
    }

    public Matcher getLargeBracket(String args) {
        return Pattern.compile("\\{(.+)}").matcher(args);
    }
}
