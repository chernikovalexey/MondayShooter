package com.twopeople.game;

import com.twopeople.game.entity.Entity;
import com.twopeople.game.entity.EntityLoader;
import com.twopeople.game.entity.Player;
import com.twopeople.game.entity.building.Launcher;
import com.twopeople.game.network.Client;
import com.twopeople.game.network.Server;
import com.twopeople.game.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Alexey
 * At 7:37 PM on 1/13/14
 */

public class GameState extends BasicGameState {
    private Camera camera;
    private World world;

    private Server server;
    private Client client;

    private int userId;
    private boolean connected = false;

    private enum LoadingState {
        NONE, INIT_WORLD, SKIP_TICK, RUNNING
    }

    private LoadingState loadingState = LoadingState.NONE;

    public boolean isServer() {
        return server != null;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        Images.init();
        EntityLoader.init();

        this.world = new World(this);
        this.camera = new Camera(gameContainer);

        String action = "s"; //Console.readString("s/c: ");

        NetworkListener listener = new NetworkListener(this, world);
        client = new Client(listener);

        if (action.equals("s")) {
            server = new Server();
            server.setClient(client);
            client.connect("localhost", "server user");
            connected = true;
        } else if (action.equals("c")) {
            client.connect("localhost", "client user");
            connected = true;
        }
    }

    public void startGame() {
        loadingState = LoadingState.INIT_WORLD;
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if (connected) {
            if (loadingState == LoadingState.INIT_WORLD) {
                world.init();
                loadingState = LoadingState.SKIP_TICK;
            } else if (loadingState == LoadingState.RUNNING) {
                camera.update(delta);
                world.update(gameContainer, delta);
            } else if (loadingState == LoadingState.SKIP_TICK) {
                loadingState = LoadingState.RUNNING;
            }
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        if (connected) {
            if (loadingState != LoadingState.RUNNING) {
                g.drawString("Loading ...", container.getWidth() / 2 - 30, container.getHeight() / 2 - 10);
            } else {
                world.render(container, g);

                g.setColor(Color.white);
                g.drawString("Type: " + (isServer() ? "server" : "client"), 10, 90);

                Launcher launcher = (Launcher) world.getSingleEntityByClass(Launcher.class);
                if (launcher != null) {
                    int seconds = (launcher.getTimer() / 1000) % 60;
                    long minutes = ((launcher.getTimer() - seconds) / 1000) / 60;

                    g.drawString("Timer: " + minutes + ":" + Console.formatNumber(seconds), 10, 110);
                }

                int i = 0;
                for (Entity e : world.getFilteredEntities(Player.class)) {
                    Player player = (Player) e;
                    g.drawString("player-" + player.getConnectionId() + ": " + player.kills + " / " + player.deaths, 655, 10 + 20 * i++);
                }
            }
        }
    }

    // =======
    // Getters

    @Override
    public int getID() {
        return 1;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public Camera getCamera() {
        return camera;
    }

    public Client getClient() {
        return client;
    }
}