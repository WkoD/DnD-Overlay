package com.github.wkod.dnd.overlay.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("messages")
@LocaleData(defaultCharset = "UTF-8", value = { @Locale("en") })
public enum Messages implements Localizable {
    CONFIGURATION_ERROR, CONFIGURATION_INVALID,

    CLIENT_DATA_INVALID_IMAGE, CLIENT_DATA_TRANSFER, CLIENT_DATA_TRANSFER_ERROR, CLIENT_DATA_TRANSFER_INVALID_TYPE,
    CLIENT_SCREEN_RESET, CLIENT_SCREEN_TOGGLE,

    SERVER_DATA_TRANSFER_ERROR,
}
