package com.twopeople.game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static final int WIDTH = 800;
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
        System.setProperty("org.lwjgl.librarypath", new File("native\\windows").getAbsolutePath());

        try {
            AppGameContainer game = new AppGameContainer(new GameController());
            game.setDisplayMode(WIDTH, HEIGHT, false);
            game.setTargetFrameRate(60);
            game.setAlwaysRender(true);
            game.start();
        } catch (Exception e) {
            e.printStackTrace();

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File("exception.txt"), true));
                writer.write(e.toString());
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}