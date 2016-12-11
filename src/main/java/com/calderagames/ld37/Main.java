package com.calderagames.ld37;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Ludum Dare 37");
        config.setWindowedMode(1280, 720);
        config.useVsync(true);
        ApplicationListener game = new LD37Game();
        new Lwjgl3Application(game, config);
    }

}
