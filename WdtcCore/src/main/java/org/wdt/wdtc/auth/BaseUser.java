package org.wdt.wdtc.auth;

import java.io.IOException;

public abstract class BaseUser {
    protected abstract User getUser() throws IOException;
}
