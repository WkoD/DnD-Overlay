package com.github.wkod.dnd.overlay.localization;

import java.util.Locale;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;

public interface Localizable {

    /**
     * Returns a localized string for the given message parameter.
     * 
     * @param localizable Localizable
     * @param args        Object...
     * @return String
     */
    public default String localize(Object... args) {
        return localize(Locale.getDefault(), args);
    }

    /**
     * Returns a localized string for the given message parameter in the given
     * locale.
     * 
     * @param locale      Locale
     * @param localizable Localizable
     * @param args        Object...
     * @return String
     */
    @SuppressWarnings("unchecked")
    public default String localize(Locale locale, Object... args) {
        IMessageConveyor conveyor = new MessageConveyor(locale);
        return conveyor.getMessage((Enum<? extends Localizable>) this, args);
    }
}
