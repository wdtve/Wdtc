package org.wdt.wdtc.ui;

import jdk.internal.loader.BuiltinClassLoader;

import java.lang.module.ModuleReference;

public class JavaFxLoader {
  public static void loadJavaFXPatch(ModuleReference reference) {
    ((BuiltinClassLoader) ClassLoader.getSystemClassLoader()).loadModule(reference);
  }
}
