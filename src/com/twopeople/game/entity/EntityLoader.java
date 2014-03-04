package com.twopeople.game.entity;

import com.twopeople.game.entity.building.Fence;
import com.twopeople.game.entity.building.Pumphouse;
import com.twopeople.game.entity.building.Railroad;
import com.twopeople.game.entity.building.Shop;
import com.twopeople.game.entity.building.Wall;
import org.newdawn.slick.Image;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by Alexey
 * At 12:46 PM on 1/29/14
 */

public class EntityLoader {
    private static HashMap<String, Class<? extends Entity>> entities = new HashMap<String, Class<? extends Entity>>();

    public static void init() {
        registerClass(Wall.class);
        registerClass(Fence.class);
        registerClass(Railroad.class);
        registerClass(Pumphouse.class);
        registerClass(Shop.class);

        registerClass(Player.class);
        registerClass(Bullet.class);
    }

    // Check for class being an instance of Entity is already performed by argument condition expression
    private static void registerClass(Class<? extends Entity> clazz) {
        String[] parts;
        String name = (parts = clazz.getName().split("\\."))[parts.length - 1].toLowerCase();
        entities.put(name, clazz);
    }

    public static boolean has(String name) {
        return entities.containsKey(name);
    }

    public static Entity getEntityInstanceByName(String name, Object[] args, Image image, EntityProperties properties) {
        Class<? extends Entity> clazz = entities.get(name.toLowerCase());

        if (clazz != null) {
            try {
                Constructor<? extends Entity> constructor = clazz.getConstructor(float.class, float.class);
                Entity entity = constructor.newInstance(args);
                entity.setImage(image);
                entity.setProperties(properties);
                return entity;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}