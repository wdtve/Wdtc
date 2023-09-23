package org.wdt.wdtc.auth.accounts;

import org.wdt.wdtc.auth.User;

import java.io.IOException;

public interface AccountsInterface {
    User getUser() throws IOException;
}
