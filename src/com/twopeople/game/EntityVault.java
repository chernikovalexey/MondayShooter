package com.twopeople.game;

import com.twopeople.game.entity.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Alexey
 * At 12:45 AM on 9/20/13
 */

public class EntityVault {
    private int size = 0;
    public int xCells, yCells;

    private EntityVaultCell[] cells;

    public class EntityVaultCell {
        public static final float WIDTH = 200;
        public static final float HEIGHT = 200;

        public int xCell, yCell;
        private ArrayList<EntityVaultItem> entities = new ArrayList<EntityVaultItem>();

        public EntityVaultCell(int xCell, int yCell) {
            this.xCell = xCell;
            this.yCell = yCell;
        }

        public float getX() {
            return xCell * WIDTH;
        }

        public float getY() {
            return yCell * HEIGHT;
        }

        public int getCellNum() {
            return computeCellNum(xCell, yCell);
        }

        public void add(Entity entity) {
            EntityVaultItem newItem = new EntityVaultItem(entity);
            EntityVaultItem duplicateItem = new EntityVaultItem(entity);
            duplicateItem.isDuplicate = true;

            add(newItem);

            for (EntityVaultCell cell : getAllCellsFor(entity)) {
                if (!cell.equals(this)) {
                    newItem.addDuplicate(cell.getCellNum());
                    cell.add(duplicateItem);
                }
            }
        }

        public void add(EntityVaultItem item) {
            synchronized (entities) {
                entities.add(item);
            }
        }

        public boolean remove(Entity entity) {
            synchronized (entities) {
                Iterator<EntityVaultItem> it = entities.iterator();

                while (it.hasNext()) {
                    EntityVaultItem item = it.next();
                    if (item.getEntity().equals(entity)) {

                        // Find and remove entity from the duplicate cells
                        if (item.hasDuplicates()) {
                            for (Integer duplicate : item.getDuplicates()) {
                                getCell(duplicate).remove(entity);
                            }
                        }

                        // Remove entity from the current cell
                        // All objects are being removed in this part
                        // Duplicate removal also comes over to this
                        it.remove();

                        return true;
                    }
                }
            }

            return false;
        }

        public ArrayList<EntityVaultItem> getEntities() {
            return entities;
        }

        public boolean isIntersecting(Entity entity) {
            return new Rectangle((int) entity.getX(), (int) entity.getY() - (int) entity.getZ(), (int) entity.getWidth(), (int) entity.getOrthogonalHeight()).intersects(getX(), getY(), WIDTH, HEIGHT);
        }
    }

    private class EntityVaultItem {
        private Entity entity;
        public boolean isDuplicate = false;
        private ArrayList<Integer> duplicateCells = new ArrayList<Integer>();

        public EntityVaultItem(Entity entity) {
            this.entity = entity;
        }

        public void addDuplicate(int cell) {
            duplicateCells.add(cell);
        }

        public boolean hasDuplicates() {
            return duplicateCells.size() > 0 && !this.isDuplicate;
        }

        public ArrayList<Integer> getDuplicates() {
            return this.duplicateCells;
        }

        public Entity getEntity() {
            return entity;
        }
    }

    public EntityVault(float worldWidth, float worldHeight) {
        this.xCells = (int) (worldWidth / EntityVaultCell.WIDTH);
        this.yCells = (int) (worldHeight / EntityVaultCell.HEIGHT);
        this.cells = new EntityVaultCell[xCells * yCells];

        for (int x = 0; x < xCells; ++x) {
            for (int y = 0; y < yCells; ++y) {
                cells[x + y * xCells] = new EntityVaultCell(x, y);
            }
        }
    }

    public void add(Entity entity) {
        int cx = getCellX(entity);
        int cy = getCellY(entity);

        entity.setCellX(cx);
        entity.setCellY(cy);

        getCell(cx, cy).add(entity);
        ++size;
    }

    public boolean remove(Entity entity) {
        if (getCell(entity.getCellX(), entity.getCellY()).remove(entity)) {
            --size;
            return true;
        }
        return false;
    }

    public void move(Entity entity) {
        remove(entity);
        add(entity);
    }

    public ArrayList<Entity> getAll() {
        ArrayList<Entity> entities = new ArrayList<Entity>();

        for (int x = 0; x < xCells; ++x) {
            for (int y = 0; y < yCells; ++y) {
                EntityVaultCell cell = getCell(x, y);
                for (EntityVaultItem i : cell.getEntities()) {
                    if (!i.isDuplicate) {
                        entities.add(i.getEntity());
                    }
                }
            }
        }

        return entities;
    }

    public ArrayList<Entity> getVisible(Camera camera) {
        ArrayList<Entity> entities = new ArrayList<Entity>();

        for (Entity e : getAll()) {
            if (camera.isVisible(e)) {
                entities.add(e);
            }
        }

        return entities;
    }

    public ArrayList<Entity> getNearbyEntities(Entity entity) {
        ArrayList<Entity> entities = new ArrayList<Entity>();
        for (EntityVaultCell cell : getAllCellsFor(entity)) {
            for (EntityVaultItem item : cell.getEntities()) {
                if (!item.getEntity().equals(entity)) {
                    entities.add(item.getEntity());
                }
            }
        }
        return entities;
    }

    public int computeCellNum(int x, int y) {
        return x + y * xCells;
    }

    public EntityVaultCell getCell(int num) {
        if (num < 0) {
            num = 0;
        }
        return cells[num];
    }

    public EntityVaultCell getCell(int x, int y) {
        return getCell(computeCellNum(x, y));
    }

    public EntityVaultCell getCellFor(Entity entity) {
        int cx = getCellX(entity);
        int cy = getCellY(entity);
        return cells[computeCellNum(cx, cy)];
    }

    public ArrayList<EntityVaultCell> getAllCellsFor(Entity entity) {
        ArrayList<EntityVaultCell> intersectingCells = new ArrayList<EntityVaultCell>();

        int cx = getCellX(entity);
        int cy = getCellY(entity);
        int ecx = cx + (int) (entity.getWidth() / EntityVaultCell.WIDTH);
        int ecy = cy + (int) (entity.getOrthogonalHeight() / EntityVaultCell.HEIGHT);

        for (int x = cx - 1; x <= ecx + 1; ++x) {
            for (int y = cy - 1; y <= ecy + 1; ++y) {
                EntityVaultCell cell = getCell(x, y);
                if (cell.isIntersecting(entity)) {
                    intersectingCells.add(cell);
                }
            }
        }

        return intersectingCells;
    }

    public int getCellX(Entity entity) {
        int cx = (int) (entity.getX() / EntityVaultCell.WIDTH);
        if (cx < 0) { cx = 0; }
        if (cx >= xCells) { cx = xCells - 1; }
        return cx;
    }

    public int getCellY(Entity entity) {
        int cy = (int) ((entity.getY() - entity.getZ()) / EntityVaultCell.HEIGHT);
        if (cy < 0) { cy = 0; }
        if (cy >= yCells) { cy = yCells - 1; }
        return cy;
    }

    public int size() {
        return size;
    }

    public Entity getById(int id) {
        for (Entity entity : getAll()) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }
}