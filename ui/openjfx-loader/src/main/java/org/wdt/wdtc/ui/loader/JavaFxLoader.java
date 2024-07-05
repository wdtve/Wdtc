package org.wdt.wdtc.ui.loader;

import jdk.internal.loader.BuiltinClassLoader;

import java.lang.module.ModuleReference;

public class JavaFxLoader {
  public static void loadJavaFXPatch(ModuleReference reference) {
    ((BuiltinClassLoader) ClassLoader.getSystemClassLoader()).loadModule(reference);
  }
}
