package org.wdt.wdtc.launch;

import java.io.File;
import java.io.IOException;

abstract class AbstractGameCommand {
    protected StringBuilder Command;

    public AbstractGameCommand() {
        Command = new StringBuilder();
    }

    abstract StringBuilder getCommand() throws IOException;

    protected void NonBreakingSpace(Object o) {
        Command.append(o).append(" ");
    }

    protected void InsertclasspathSeparator(File file) {
        InsertclasspathSeparator(file.getAbsolutePath());
    }

    protected void InsertclasspathSeparator(String str) {
        Command.append(str).append(";");
    }

    protected void InsertSpace(String str) {
        Command.append(str).append(" ");
    }

    protected void NonBreakingSpace(String str, Object string) {
        NonBreakingSpace(str + string);
    }
}
