package com.twopeople.game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Main {
    public static final int WIDTH = 720;
    public static final int HEIGHT = WIDTH * 3 / 4;

    public static class GameController extends StateBasedGame {
        public GameController() {
            super("Monday Shooter");
        }

        @Override
        public void initStatesList(GameContainer gameContainer) throws SlickException {
            addState(new GameState());
            enterState(1);
        }
    }

    public static void main(String[] args) {
        try {
            AppGameContainer game = new AppGameContainer(new GameController());
            game.setDisplayMode(WIDTH, HEIGHT, false);
            game.setAlwaysRender(true);
            game.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}