package com.github.wkod.dnd.overlay.client.fx.localization;

import com.github.wkod.dnd.overlay.localization.Localizable;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("label")
@LocaleData(defaultCharset = "UTF-8", value = { @Locale("en") })
public enum Label implements Localizable {
    CONFIRM,
    CANCEL,
    SAVE,
    
    IMAGE,

    CLIENT_BUTTON_RESET,
    CLIENT_BUTTON_TOGGLE,
    CLIENT_CHECKBOX_BACKGROUND,
    CLIENT_CHECKBOX_DISPLAY_NAME,

    CLIENT_MENU,
    CLIENT_MENU_READ_SCREENS,
    CLIENT_MENU_TOGGLE_CONSOLE,
    CLIENT_MENU_EXIT,

    CLIENT_CONFIGURATION,
    CLIENT_CONFIGURATION_CLIENT,
    CLIENT_CONFIGURATION_SERVER,

    CLIENT_SCREENS,
    CLIENT_SCREENS_ALL,
    CLIENT_SCREENS_SCREEN,
}
