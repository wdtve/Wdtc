package org.wdt;

import java.io.IOException;

public class list {
    public static void main(String[] args) throws IOException, InterruptedException {
        String str = "1.19.4";
        String str1 = str.substring(0, str.indexOf("-"));
        String str2 = str.substring(str1.length() + 1);
        System.out.println(str2);
    }
}
