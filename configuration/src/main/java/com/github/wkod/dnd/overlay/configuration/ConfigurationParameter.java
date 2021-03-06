package com.github.wkod.dnd.overlay.configuration;

import java.util.Locale;

import com.github.wkod.dnd.overlay.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.localization.Messages;
import com.rits.cloning.Cloner;

public class ConfigurationParameter<T> {

    protected final String name;

    private T value;

    protected final Class<T> clazz;

    protected final ConfigurationValidator<T> validator;

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
    public final String name() {
        return this.name;
    }

    /**
     * Get the value of the property cast into its specific type.
     * 
     * @return T
     */
    public final T get() {
        return this.value;
    }

    /**
     * Set value of the property
     * 
     * @param value T
     */
    public final void set(T value) {
        if (!validate(value)) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, String.valueOf(value));
        }

        this.value = value;
    }

    /**
     * Validates value of this configuration parameter.
     * 
     * @return boolean
     */
    public boolean validate() {
        return validate(this.value);
    }

    /**
     * Validates given value for this configuration parameter.
     * 
     * @param value T
     * @return boolean
     */
    public final boolean validate(T value) {
        return this.validator.validate(value);
    }

    /**
     * Return value as String.
     * 
     * @return String
     */
    @Override
    public final String toString() {
        return String.valueOf(value);
    }

    /**
     * Set value from String.
     * 
     * @param valuestring String
     */
    public final void fromString(String valuestring) {
        if (valuestring == null) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, valuestring, name);
        }

        set(convert(valuestring));
    }
    
    /**
     * Read String into value.
     * 
     * @param valuestring String
     */
    public void load(String valuestring) {
        fromString(valuestring);
    }
    
    /**
     * Save value into String.
     * 
     * @return String
     */
    public String save() {
        return toString();
    }

    /**
     * Clone this configuration parameter. There is NO validation of values done
     * here.
     * 
     * @return ConfigurationParameter<T>
     */
    protected ConfigurationParameter<T> clone() {
        Cloner cloner = new Cloner();
        ConfigurationParameter<T> clone = new ConfigurationParameter<>(name, clazz, validator);
        // only clone value as other values are final
        clone.value = cloner.deepClone(this.value);
        return clone;
    }

    /**
     * True if configuration has defined possible values, false otherwise.
     * 
     * @return boolean
     */
    public final boolean hasValues() {
        return getValues() != null;
    }

    /**
     * Lists all possible values, null if not limited.
     * 
     * @return T[]
     */
    @SuppressWarnings("unchecked")
    public final T[] getValues() {
        if (clazz.isEnum()) {
            return clazz.getEnumConstants();
        } else if (clazz.isAssignableFrom(Boolean.class)) {
            return (T[]) new Object[] { Boolean.TRUE, Boolean.FALSE };
        } else {
            return null;
        }
    }

    /**
     * Convert String value to T.
     * 
     * @param valuestring String
     * @return T
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected final T convert(String valuestring) {

        T value;

        try {
            if (clazz.isAssignableFrom(Boolean.class)) {
                value = (T) convertBoolean(valuestring);
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

    /**
     * Convert String to Boolean value and throw exception for anything other than
     * true or false (ignore case).
     * 
     * @param valuestring String
     * @return Boolean
     */
    private final Boolean convertBoolean(String valuestring) {
        if (valuestring.equalsIgnoreCase(Boolean.TRUE.toString())
                || valuestring.equalsIgnoreCase(Boolean.FALSE.toString())) {
            return Boolean.valueOf(valuestring);
        } else {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID_BOOLEAN, valuestring, name);
        }
    }
}
