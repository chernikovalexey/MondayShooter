package com.twopeople.game.particle.debris;

import com.twopeople.game.Camera;
import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.Player;
import com.twopeople.game.particle.MSParticle;
import com.twopeople.game.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;

import java.util.Collection;

public class Debris extends MSParticle {
    public boolean finished = false;

    public boolean hasShadow = true;
    public boolean collides = true;

    public Debris(World world, ParticleSystem engine) {
        super(world, engine);
    }

    @Override
    public void update(int delta) {
        float px = x;
        float py = y;

        super.update(delta);

        this.x = px;
        this.y = py;

        float d = delta * 0.150f;
        int steps = (int) Math.sqrt(velx * velx + vely * vely + velz * velz) + 1;

        for (int i = 0; i < steps; ++i) {
            partialMove(d * velx / steps, d * vely / steps, delta);
        }

        if (life > 0) {
            z += d * velz;
        }
    }

    private void partialMove(float dx, float dy, int delta) {
        x += dx;
        y += dy;

        if (collides) {
            associatedEntity.setX(x);
            associatedEntity.setY(y);
            associatedEntity.setZ(z);
            Collection<Entity> nearby = world.getEntities().getNearbyEntities(associatedEntity);

            for (Entity e : nearby) {
                if (associatedEntity.collidesWith(e) && !(e instanceof Player)) {
                    Vector2f hitSide = e.getHitSideVector(associatedEntity);

                    Vector2f u = hitSide.getPerpendicular();
                    Vector2f w = hitSide.sub(u);
                    w.x *= 0.01f;
                    w.y *= 0.01f;
                    u.x *= 0.001f;
                    u.y *= 0.001f;
                    Vector2f v2 = w.sub(u);

                    v2.x *= delta * 0.05f;
                    v2.y *= delta * 0.05f;

                    velx = v2.x;
                    vely = v2.y;

                    int i = 0;
                    while (associatedEntity.collidesWith(e)) {
                        x -= dx;
                        associatedEntity.setX(x);
                        if (!associatedEntity.collidesWith(e)) { break; }

                        y -= dy;
                        associatedEntity.setY(y);
                        if (!associatedEntity.collidesWith(e)) { break; }

                        if (++i >= 10) {
                            kill();
                            break;
                        }
                    }

                    break;
                }
            }
        }
    }

    @Override
    public void render(GameContainer container, Camera camera, Graphics g) {
        if (inUse()) {
            // Because of not extending `Entity` I am repeating myself
            if (hasShadow) {
                float shadowDistance = z - size;
                float shadowOpacity = 0.1f;

                g.setColor(new Color(0, 0, 0, shadowOpacity));
                float sw = size * 1.5f;
                g.fillOval(camera.getX(x + (size - sw) / 2), camera.getY(y + size + shadowDistance - z), sw, size / 1.75f);
            }

            super.render(container, camera, g);
        }
    }
}