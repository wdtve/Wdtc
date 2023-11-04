package org.wdt.wdtc.core.download.game;

public class VersionNotFoundException extends RuntimeException {

    public VersionNotFoundException(String message) {
        super(message);
    }

    public VersionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionNotFoundException(Throwable cause) {
        super(cause);
    }

    public VersionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
