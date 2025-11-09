package com.Team10.Team10Maze;

import com.badlogic.gdx.Game;

public class mazeGame extends Game {


    /**
     * Initalises the game object used to set the screen across the entire application
     */
    @Override
    public void create() {
        setScreen(new mainMenu(this)); // setting mainMenu.java to be shown, passing through "this" instance

    }

}
