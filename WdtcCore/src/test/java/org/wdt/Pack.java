package org.wdt;

import org.wdt.download.dependency.DefaultDependency;
import org.wdt.download.dependency.DependencyDownload;

public class Pack {

    public static void main(String[] args) {
        DefaultDependency dependency = new DependencyDownload("net.minecraft:client:1.19.4-20230314.122934:slim");
        System.out.println(dependency.formJar());
    }

}
