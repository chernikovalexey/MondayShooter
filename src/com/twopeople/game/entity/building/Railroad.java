package com.twopeople.game.entity.building;

import com.twopeople.game.Camera;
import com.twopeople.game.Team;
import com.twopeople.game.entity.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * Created by Alexey
 * At 4:45 PM on 2/22/14
 */

public class Railroad extends Entity {
    public static float WIDTH = 128;
    public static float HEIGHT = 0;
    public static float DEPTH = 64;

    private Team team;

    public Railroad(float x, float y) {
        super(x, y, 0, WIDTH, HEIGHT, DEPTH, true);

        this.team = new Team(Team.REBELS);
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        g.drawImage(image, camera.getX(this), camera.getY(this));
        g.setColor(new Color(255, 255, 255, 120));
        //g.fillRect(camera.getX(getBBCentre().x),camera.getY(getBBCentre().y),width,depth);
        Shape shape = getBB();
        shape.setX(camera.getX(shape.getX()));
        shape.setY(camera.getY(shape.getY()));
        g.fill(shape);
        g.setColor(Color.white);
        g.drawString(id + ", " + team.id, camera.getX(this), camera.getY(this));
    }

    public Team getTeam() {
        return team;
    }
}