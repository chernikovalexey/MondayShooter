package com.twopeople.game;

import java.util.Scanner;

public class Console {
    public static String readString(String placeholder) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(placeholder);
        return scanner.hasNextLine() ? scanner.nextLine() : "";
    }

    public static String formatNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }
}