package com.github.wkod.dnd.overlay.api.exception;

public class OlRuntimeException extends RuntimeException {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -6321340746005144778L;

    public OlRuntimeException(String message) {
        super(message);
    }

    public OlRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
