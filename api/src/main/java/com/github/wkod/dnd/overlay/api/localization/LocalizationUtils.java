package com.github.wkod.dnd.overlay.api.localization;

import java.util.Locale;

public final class LocalizationUtils {

    /**
     * Private constructor.
     */
    private LocalizationUtils() {
    }

    /**
     * Returns true if the given locale is supported.
     * 
     * @param locale Locale
     * @return boolean
     */
    public static boolean isSupported(Locale locale) {
        Messages[] values = Messages.values();

        if (values.length > 0) {
            try {
                values[0].localize(locale);
            } catch (Throwable e) {
                return false;
            }
        }

        return true;
    }
}
