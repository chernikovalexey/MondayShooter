package com.twopeople.game;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by Alexey
 * At 12:46 PM on 1/29/14
 */

public class EntityLoader {
    public static HashMap<String, Class<?>> entities = new HashMap<String, Class<?>>();

    public static void init() {
        registerClass(Wall.class);
        registerClass(Player.class);
        registerClass(Bullet.class);
    }

    private static void registerClass(Class<?> clazz) {
        boolean isEntity = clazz.getSuperclass().equals(Entity.class);
        if (isEntity) {
            String[] parts;
            String name = (parts = clazz.getName().split("\\."))[parts.length - 1].toLowerCase();
            entities.put(name, clazz);
        }
    }

    public static Entity getEntityInstanceByName(String name, Object[] args) {
        Class<?> clazz = entities.get(name.toLowerCase());
        if (clazz != null) {
            try {
                Constructor<?> constructor = clazz.getConstructor(float.class, float.class);
                return (Entity) constructor.newInstance(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Entity();
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
    }
}