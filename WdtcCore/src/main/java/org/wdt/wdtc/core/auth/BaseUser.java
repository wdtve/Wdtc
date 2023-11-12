package org.wdt.wdtc.core.auth;

import java.io.IOException;

public abstract class BaseUser {
  protected abstract User getUser() throws IOException;
}
