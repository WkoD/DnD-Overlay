package com.github.wkod.dnd.overlay.configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.wkod.dnd.overlay.exception.OlRuntimeException;
import com.github.wkod.dnd.overlay.localization.Messages;
import com.rits.cloning.Cloner;

public class ScreenConfigurationParameter<T> extends ConfigurationParameter<T> {

    /**
     * Map with additional configurations per screen (index start at 0).
     * Default value (super.value) is not part of this map.
     */
    private final Map<Integer, T> map = new LinkedHashMap<>();

    /**
     * Constructor.
     * 
     * @param name  String
     * @param clazz Class<T>
     */
    protected ScreenConfigurationParameter(String name, Class<T> clazz) {
        this(name, clazz, null);
    }

    /**
     * Constructor.
     * 
     * @param name      String
     * @param clazz     Class<T>
     * @param validator ConfigurationValidator<T>
     */
    protected ScreenConfigurationParameter(String name, Class<T> clazz, ConfigurationValidator<T> validator) {
        super(name, clazz, validator);
    }

    public T get(int index) {
        T value = map.get(index);

        if (value != null) {
            return value;
        } else {
            return get();
        }
    }

    public void set(T value, int index) {
        if (!validate(value) || index < 0) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, String.valueOf(value));
        }

        map.put(index, value);
    }

    @Override
    public boolean validate() {
        // validate default value
        if (!super.validate()) {
            return false;
        }

        for (T value : map.values()) {
            if (!super.validate(value)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        List<String> list = new ArrayList<>();
        
        for (Map.Entry<Integer, T> entry : map.entrySet()) {
            if (list.size() > entry.getKey()) {
                list.set(entry.getKey(), String.valueOf(entry.getValue()));
            } else {
                for (int i = entry.getKey() - list.size(); i > 0; --i) {
                    list.add(null);
                }
                
                list.add(String.valueOf(entry.getValue()));
            }
        }
        
        // add default value
        sb.append(super.toString());
        
        for (String v : list) {
            sb.append(Configuration.LIST_DELIMITER);
            
            if (v != null) {
                sb.append(v);
            }
        }

        return sb.toString();
    }

    @Override
    public void fromString(String valuestring) {
        if (valuestring == null) {
            throw new OlRuntimeException(Messages.CONFIGURATION_INVALID, valuestring, name);
        }

        String[] vsplit = valuestring.split(Configuration.LIST_DELIMITER);
        map.clear();

        // set default value
        super.fromString(vsplit[0]);

        // set additional values
        for (int i = 1; i < vsplit.length; ++i) {
            String v = vsplit[i];

            // set only non placeholder values
            if (v != null && !"".equals(v)) {
                // screen index start at 0
                map.put(i - 1, super.convert(v));
            }
        }
    }

    @Override
    protected ConfigurationParameter<T> clone() {
        Cloner cloner = new Cloner();
        ScreenConfigurationParameter<T> clone = new ScreenConfigurationParameter<>(name, clazz, validator);

        for (Map.Entry<Integer, T> entry : map.entrySet()) {
            // only clone value as other values are final
            clone.map.put(entry.getKey(), cloner.deepClone(entry.getValue()));
        }

        return clone;
    }
}
