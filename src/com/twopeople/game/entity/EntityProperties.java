package com.twopeople.game.entity;

import java.util.HashMap;

/**
 * Created by Alexey
 * At 10:10 PM on 2/22/14
 */

public class EntityProperties {
    private HashMap<String, Integer[]> properties = new HashMap<String, Integer[]>();

    public EntityProperties(String list) {
        if (!list.isEmpty()) {
            String[] parts = list.split(";");

            for (String part : parts) {
                String[] parameter = part.split("=");
                String[] valuesRaw = parameter[1].split(",");

                int len = valuesRaw.length;
                Integer[] values = new Integer[len];

                for (int i = 0; i < len; ++i) {
                    values[i] = Integer.parseInt(valuesRaw[i]);
                }

                properties.put(parameter[0], values);
            }
        }
    }

    public Integer[] getValues(String key) {
        return properties.get(key);
    }

    public Integer getFirstValue(String key) {
        Integer[] values = getValues(key);
        return values == null ? 0 : values[0];
    }
}