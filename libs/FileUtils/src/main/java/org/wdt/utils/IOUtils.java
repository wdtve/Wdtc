package org.wdt.utils;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class IOUtils {
    public static String toString(InputStream inputStream) throws IOException {
        return toString(new InputStreamReader(inputStream));
    }

    public static void copy(Reader reader, Writer writer) throws IOException {
        char[] array = new char[100];
        int n;
        while (-1 != (n = reader.read(array))) {
            writer.write(array, 0, n);
        }
        reader.close();
        writer.close();
    }

    public static String toString(Reader reader) throws IOException {
        StringWriter stringWriter = new StringWriter();
        copy(reader, stringWriter);
        return stringWriter.toString();
    }

    public static void copyLarge(InputStream inputStream, OutputStream outputStream) throws IOException {
        int m;
        byte[] data = new byte[1024];
        while ((m = inputStream.read(data, 0, 1024)) >= 0) {
            outputStream.write(data, 0, m);
        }
        inputStream.close();
        outputStream.close();
    }

    public static void write(Writer writer, String s) throws IOException {
        writer.write(s);
        writer.close();
    }

    public static List<String> readLines(Reader reader) {
        return new BufferedReader(reader).lines().collect(Collectors.toList());
    }

    public static List<String> readLines(InputStream inputStream) {
        return readLines(new InputStreamReader(inputStream));
    }

    public static String toString(URL url) throws IOException {
        return toString(url.openStream());
    }
}
