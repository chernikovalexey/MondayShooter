package com.twopeople.game;

import java.util.Scanner;

public class Console {
    public static String readString(String placeholder) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(placeholder);

        while (scanner.hasNextLine()) {
            return scanner.nextLine();
        }

        return "";
    }
}