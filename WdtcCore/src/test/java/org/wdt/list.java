package org.wdt;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.EmptyFileFilter;

import java.io.File;
import java.util.Collection;

public class list {
    public static void main(String[] args) {
        Collection<File> list = FileUtils.listFilesAndDirs(new File(System.getProperty("user.dir")), EmptyFileFilter.NOT_EMPTY, null);
        for (File s : list) {
            System.out.println(s.getName());
        }
    }
}
