package com.Team10.Team10Maze;

import com.badlogic.gdx.Game;

public class mazeGame extends Game {

    @Override
    public void create() {
        setScreen(new mainMenu(this)); // setting mainMenu.java to be shown, passing through "this" instance

    }

}
