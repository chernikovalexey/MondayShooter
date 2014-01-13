package com.twopeople.game;

import com.twopeople.game.network.Client;
import com.twopeople.game.network.Server;
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
    private World world;

    private Server server;
    private Client client;

    private int userId;
    private boolean connected = false;

    public boolean isServer() {
        return server != null;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        world = new World(this);

        String action = Console.readString("s/c: ");

        NetworkListener listener = new NetworkListener(this, world);
        Client client = new Client(listener);

        if (action.equals("s")) {
            server = new Server();
            server.setClient(client);
            client.connect("localhost", Console.readString("Nickname: "));
            connected = true;
        } else if (action.equals("c")) {
            client.connect(Console.readString("IP: "), Console.readString("Nickname: "));
            connected = true;
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        if (connected) {
            world.update(gameContainer, i);
        }
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        if (connected) {
            world.render(gameContainer, g);
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
}