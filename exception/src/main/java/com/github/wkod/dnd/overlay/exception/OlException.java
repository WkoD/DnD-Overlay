package com.github.wkod.dnd.overlay.exception;

import com.github.wkod.dnd.overlay.localization.Localizable;

public class OlException extends Exception {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 3383973031359448036L;

    /**
     * Constructor.
     * 
     * @param message Localizable
     * @param args    Object...
     */
    public OlException(Localizable message, Object... args) {
        super(message.localize(args),
                (args[args.length - 1] instanceof Throwable) ? (Throwable) (args[args.length - 1]) : null);
    }
}
