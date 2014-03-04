package com.twopeople.game.entity;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.Images;
import com.twopeople.game.entity.building.Railroad;
import com.twopeople.game.world.pathfinder.Path;
import com.twopeople.game.world.pathfinder.RailroadRouter;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Alexey
 * At 5:21 PM on 2/22/14
 */

public class Railcar extends Entity {
    public static float WIDTH = 35;
    public static float HEIGHT = 20;
    public static float DEPTH = 28;

    private RailroadRouter router;
    private Path path;

    public Railcar(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);
        setSpeed(3.5f);

        this.range = 45f;
    }

    @Override
    public void init() {
        this.router = new RailroadRouter(world.getFilteredEntities(Railroad.class));
        this.path = router.construct(world.getEntities().getById(151), world.getEntities().getById(175));
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        if (delta > 30) {
            return;
        }

        if (carrying != null && path != null) {
            if (!path.isFinished()) {
                Vector2f goalPosition = path.getCurrentTarget(this);
                setMovingDirectionToPoint(goalPosition.x, goalPosition.y);
            }

            float px = x;
            float py = y;

            super.update(container, delta, entities);
            movingDirection.set(0, 0);

            float dx = x - px;
            float dy = y - py;
            carrying.setX(carrying.getX() + dx);
            carrying.setY(carrying.getY() + dy);
            carrying.onBoundMove();
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(Images.railcar.getSprite(0, 0), camera.getX(this), camera.getY(this));

        g.setColor(new Color(213, 43, 22, 105));
        Shape bb = getBB();
        bb.setX(camera.getX(bb.getX()));
        bb.setY(camera.getY(bb.getY()));
        g.fill(bb);

        //router.render(camera, g);
        //path.render(camera,g);
    }
}