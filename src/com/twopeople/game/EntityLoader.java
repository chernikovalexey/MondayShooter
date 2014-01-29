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

    public static boolean has(String name) {
        return entities.containsKey(name);
    }

    public static Entity getEntityInstanceByName(String name, Object[] args, int[] skin) {
        Class<?> clazz = entities.get(name.toLowerCase());
        if (clazz != null) {
            try {
                Constructor<?> constructor = clazz.getConstructor(float.class, float.class);
                Entity entity = (Entity) constructor.newInstance(args);
                --skin[0];
                --skin[1];
                entity.setSkin(skin);
                return entity;
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