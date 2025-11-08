package com.Team10.Team10Maze;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SpecialTiles {
    // special locations
    private Rectangle goalArea;
    private Rectangle addTimeArea;
    private Rectangle decreaseTimeArea;
    private boolean addTimeCollected = false;
    private boolean decreaseTimeCollected = false;
    private int specialCounter = 0;

    public SpecialTiles(Rectangle goalArea, Rectangle addTimeArea, Rectangle decreaseTimeArea) {
        this.goalArea = goalArea;
        this.addTimeArea = addTimeArea;
        this.decreaseTimeArea = decreaseTimeArea;
    }

    // check goal first - end game
    public boolean isGoalReached(Vector2 playerPosition) {
        if (goalArea != null && goalArea.contains(playerPosition)) {
            System.out.println("GOAL REACHED! You win!");
            return true;
        }
        return false;
    }

    // check addTime
    public boolean checkAddTimeCollision(Vector2 playerPosition) {
        if (!addTimeCollected && addTimeArea != null && addTimeArea.contains(playerPosition)) {
            System.out.println("addtime collected! +5 seconds");
            specialCounter += 1;
            addTimeCollected = true;
            return true;
        }
        return false;
    }

    // check decreaseTime
    public boolean checkDecreaseTimeCollision(Vector2 playerPosition) {
        if (!decreaseTimeCollected && decreaseTimeArea != null && decreaseTimeArea.contains(playerPosition)) {
            System.out.println("decreaseTime collected! +5 seconds");
            specialCounter += 1;
            decreaseTimeCollected = true;
            return true;
        }
        return false;
    }

    public int getSpecialCounter() {
        return specialCounter;
    }

    public boolean isAddTimeCollected() {
        return addTimeCollected;
    }

    public boolean isDecreaseTimeCollected() {
        return decreaseTimeCollected;
    }
}
