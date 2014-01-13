package com.twopeople.game;

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

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        world = new World(this);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        world.update(gameContainer, i);
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        world.render(gameContainer, g);
    }

    // =======
    // Getters

    @Override
    public int getID() {
        return 1;
    }
}