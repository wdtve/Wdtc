package org.wdt.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    public static String readFileToString(File file) throws IOException {
        return readFileToString(file.toPath());
    }

    public static String readFileToString(Path FilePath) throws IOException {
        return IOUtils.toString(Files.newBufferedReader(FilePath));
    }

    public static void writeStringToFile(File file, String s) throws IOException {
        writeStringToFile(file.toPath(), s);
    }

    public static void writeStringToFile(Path path, String s) throws IOException {
        touch(path);
        IOUtils.write(Files.newBufferedWriter(path), s);
    }

    public static void touch(File file) throws IOException {
        touch(file.toPath());
    }

    public static void touch(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } else {
            Files.setLastModifiedTime(path, FileTime.from(Instant.now()));
        }
    }

    public static List<String> readFileToLines(File file) throws IOException {
        return readFileToLines(file.toPath());
    }

    public static List<String> readFileToLines(Path path) throws IOException {
        return IOUtils.readLines(Files.newBufferedReader(path));
    }

    public static void delete(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    public static void delete(File file) throws IOException {
        delete(file.toPath());
    }

    public static long sizeOf(Path path) throws IOException {
        return Files.size(path);
    }

    public static void cleanDirectory(File file) throws IOException {
        cleanDirectory(file.toPath());
    }

    public static void cleanDirectory(Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            throw new IOException();
        }
        for (Path path1 : Files.newDirectoryStream(path)) {
            if (Files.isDirectory(path1)) {
                cleanDirectory(path1);
                deleteDirectory(path1);
            } else {
                FileUtils.delete(path1);
            }
        }
    }

    public static long sizeOfDirectory(Path path) throws IOException {
        long DirSize = 0;
        for (Path path1 : Files.list(path).toList()) {
            if (Files.isDirectory(path1)) {
                sizeOfDirectory(path1);
            } else {
                DirSize = sizeOf(path1);
            }
        }
        return DirSize;
    }

    public static long sizeOfDirectory(File file) throws IOException {
        return sizeOfDirectory(file.toPath());
    }

    public static void deleteDirectory(File file) throws IOException {
        deleteDirectory(file.toPath());
    }

    public static void deleteDirectory(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }
        cleanDirectory(path);
        delete(path);
    }

    public static void copyFile(File srcFile, File file) throws IOException {
        Files.copy(Files.newInputStream(srcFile.toPath()), file.toPath());
    }

    public static DirectoryStream<Path> PathList(Path path) throws IOException {
        return Files.newDirectoryStream(path);
    }

    public static List<File> FileList(File file) {
        return Arrays.stream(file.listFiles()).toList();
    }

    public static InputStream newInputStream(File file) throws IOException {
        return Files.newInputStream(file.toPath());
    }

    public static OutputStream newOutputStream(File file) throws IOException {
        return Files.newOutputStream(file.toPath());
    }

    public static void createDirectories(File file) {
        try {
            Files.createDirectories(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
