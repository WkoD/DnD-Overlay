package com.github.wkod.dnd.overlay.configuration;

import java.util.Locale;

import com.github.wkod.dnd.overlay.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.localization.Messages;

public class ConfigurationParameter<T> {

    private final String name;

    private T value;

    private final Class<T> clazz;

    private final ConfigurationValidator<T> validator;

    /**
     * Constructor.
     * 
     * @param name  String
     * @param clazz Class<T>
     */
    protected ConfigurationParameter(String name, Class<T> clazz) {
        this(name, clazz, null);
    }

    /**
     * Constructor.
     * 
     * @param name      String
     * @param clazz     Class<T>
     * @param validator ConfigurationValidator<T>
     */
    protected ConfigurationParameter(String name, Class<T> clazz, ConfigurationValidator<T> validator) {
        this.name = name;
        this.clazz = clazz;

        if (validator != null) {
            this.validator = validator;
        } else {
            this.validator = (T value) -> {
                return value != null;
            };
        }
    }

    /**
     * Get name of property.
     * 
     * @return String
     */
    public String name() {
        return this.name;
    }

    /**
     * Get the value of the property cast into its specific type.
     * 
     * @return T
     */
    public T get() {
        return this.value;
    }

    /**
     * Set value of the property
     * 
     * @param value T
     */
    public void set(T value) {
        if (!validate(value)) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, String.valueOf(value));
        }

        this.value = value;
    }

    /**
     * Validates String value for this configuration parameter.
     * 
     * @param value T
     * @return boolean
     */
    public boolean validate(T value) {
        return this.validator.validate(value);
    }

    /**
     * Return value as String.
     * 
     * @return String
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Set value from String.
     * 
     * @param valuestring String
     */
    public void fromString(String valuestring) {
        if (valuestring == null) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, valuestring, name);
        }

        set(convert(valuestring));
    }

    /**
     * True if the T class is an Enum, false otherwise.
     * 
     * @return boolean
     */
    public boolean isTypeEnum() {
        return clazz.isEnum();
    }

    /**
     * Lists all possible Enum types if T class is an Enum, null otherwise.
     * 
     * @return T[]
     */
    public T[] getEnumValues() {
        if (!isTypeEnum()) {
            return null;
        }

        return clazz.getEnumConstants();
    }

    /**
     * Convert String value to T.
     * 
     * @param valuestring String
     * @return T
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private T convert(String valuestring) {

        T value;

        try {
            if (clazz.isAssignableFrom(Boolean.class)) {
                value = (T) Boolean.valueOf(valuestring);
            } else if (clazz.isAssignableFrom(Integer.class)) {
                value = (T) Integer.valueOf(valuestring);
            } else if (clazz.isAssignableFrom(Double.class)) {
                value = (T) Double.valueOf(valuestring);
            } else if (clazz.isAssignableFrom(Locale.class)) {
                value = (T) Locale.forLanguageTag(valuestring);
            } else if (clazz.isEnum()) {
                value = (T) Enum.valueOf((Class<? extends Enum>) clazz, valuestring);
            } else {
                value = (T) valuestring;
            }
        } catch (NumberFormatException e) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, valuestring, name, e);
        }

        if (value == null || !validator.validate(value)) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, valuestring, name);
        }

        return value;
    }
}
