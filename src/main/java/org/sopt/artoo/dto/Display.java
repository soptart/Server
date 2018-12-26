package org.sopt.artoo.dto;

public class Display {
    private static Display ourInstance = new Display();

    public static Display getInstance() {
        return ourInstance;
    }

    private Display() {
    }
}
