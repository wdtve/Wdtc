package org.wdt.wdtc.console.utils;

import org.apache.commons.cli.Option;

public class OptionUtils {
  public static Option getOption(String longAge, String arg, boolean hasArgs, String desc) {
    return Option.builder(arg).longOpt(longAge).hasArg(hasArgs).desc(desc).required(false).build();
  }

  public static Option getOption(String longAge, String arg, String desc) {
    return getOption(longAge, arg, false, desc);
  }

  public static Option getOption(String longAge, String arg) {
    return getOption(longAge, arg, false, "No Description");
  }

  public static Option getOption(String longAge, String arg, boolean hasArg) {
    return getOption(longAge, arg, hasArg, "No Description");
  }
}
