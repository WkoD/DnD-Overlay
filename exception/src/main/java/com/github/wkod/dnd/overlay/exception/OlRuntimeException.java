package com.github.wkod.dnd.overlay.exception;

import com.github.wkod.dnd.overlay.localization.Localizable;

public class OlRuntimeException extends RuntimeException {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -6321340746005144778L;

    /**
     * Constructor.
     * 
     * @param message Localizable
     * @param args    Object...
     */
    public OlRuntimeException(Localizable message, Object... args) {
        super(message.localize(args),
                (args[args.length - 1] instanceof Throwable) ? (Throwable) (args[args.length - 1]) : null);
    }
}
