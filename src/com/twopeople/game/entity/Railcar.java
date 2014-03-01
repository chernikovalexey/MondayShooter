package com.twopeople.game.entity;

import com.twopeople.game.Camera;
import com.twopeople.game.EntityVault;
import com.twopeople.game.Images;
import com.twopeople.game.entity.building.Railroad;
import com.twopeople.game.world.pathfinder.RailPath;
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
    public static float WIDTH = 70;
    public static float HEIGHT = 50;
    public static float DEPTH = 50;

    private RailroadRouter router;
    private RailPath path;

    public Railcar(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);
        setSpeed(1.2f);
    }

    @Override
    public void init() {
        this.router = new RailroadRouter(world.getFilteredEntities(Railroad.class));
        this.path = router.construct(world.getEntities().getById(108), world.getEntities().getById(111), null);

        /*System.out.println();
        for (Entity node : path.getNodes()) {
            System.out.print(node.getId() + " -> ");
        }
        System.out.println();*/
    }

    @Override
    public void update(GameContainer container, int delta, EntityVault entities) {
        if (delta > 30) {
            return;
        }

        if (!path.isFinished()) {
            Vector2f target = path.getCurrentTarget(this);
            System.out.println(target);
            setMovingDirectionToPoint(target.x, target.y);
        }

        super.update(container, delta, entities);
        movingDirection.set(0, 0);
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(Images.railcar.getSprite(0, 0), camera.getX(this), camera.getY(this));

        g.setColor(new Color(213, 43, 22, 105));
        Shape bb = getBB();
        bb.setX(camera.getX(bb.getX()));
        bb.setY(camera.getY(bb.getY()));
        g.fill(bb);
    }
}