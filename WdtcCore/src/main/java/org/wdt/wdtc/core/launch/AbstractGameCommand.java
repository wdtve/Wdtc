package org.wdt.wdtc.core.launch;

import org.wdt.wdtc.core.utils.StringUtils;

import java.io.File;
import java.io.IOException;

abstract class AbstractGameCommand {
  protected final StringBuilder command;

  public AbstractGameCommand() {
    command = new StringBuilder();
  }

  abstract StringBuilder getCommand() throws IOException;

  protected void nonBreakingSpace(Object o) {
    command.append(o).append(StringUtils.STRING_SPACE);
  }

  protected void insertclasspathSeparator(File file) {
    insertclasspathSeparator(file.getAbsolutePath());
  }

  protected void insertclasspathSeparator(String str) {
    command.append(str).append(";");
  }

  protected void insertSpace(String str) {
    command.append(str).append(StringUtils.STRING_SPACE);
  }

  protected void nonBreakingSpace(String str, Object string) {
    nonBreakingSpace(str + string);
  }
}
