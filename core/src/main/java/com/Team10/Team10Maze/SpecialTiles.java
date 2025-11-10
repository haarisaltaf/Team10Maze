package com.Team10.Team10Maze;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SpecialTiles {
    // special locations
    private Rectangle goalArea;
    private Rectangle addTimeArea;
    private Rectangle decreaseTimeArea;
    private Rectangle randomTeleportArea;
    private boolean addTimeCollected = false;
    private boolean decreaseTimeCollected = false;
    private boolean randomTeleportTriggered = false;
    private int specialCounter = 0;

    /**
     * Initialises the special tiles manager with the provided areas. Each libgdx Rectangle is defined
     * in the map1Maze.java class file and passed-through into this function to check if player
     * has collided with any of these sections.
     *
     * @param goalArea Tiled Rectangle defining the goal area on the map
     * @param addTimeArea Tiled Rectangle defining the add time powerup area on the map
     * @param decreaseTimeArea Tiled Rectangle defining the decrease time debuff area on the map
     */
    public SpecialTiles(Rectangle goalArea, Rectangle addTimeArea, Rectangle decreaseTimeArea, Rectangle randomTeleportArea) {
        this.goalArea = goalArea;
        this.addTimeArea = addTimeArea;
        this.decreaseTimeArea = decreaseTimeArea;
        this.randomTeleportArea = randomTeleportArea;
    }

    /**
    * Checks if the player's current location is within the goal Rectangle area.
    * Runs on every render() of map1Maze.java under the handleInput() function.
    *
    * @param playerPosition Vector of the player's current position
    * @return True if player is in the goal area, false otherwise
    */
    public boolean isGoalReached(Vector2 playerPosition) {
        if (goalArea != null && goalArea.contains(playerPosition)) {
            System.out.println("GOAL REACHED! You win!");
            return true;
        }
        return false;
    }

    /**
     * Checks if the player's current location is within the addTime Rectangle area.
     * Runs on every render() of map1Maze.java under the handleInput() function.
     *
     * @param playerPosition Vector of the player's current position
     * @return True if player collected the add time powerup, false otherwise
     */
    public boolean checkAddTimeCollision(Vector2 playerPosition) {
        if (!addTimeCollected && addTimeArea != null && addTimeArea.contains(playerPosition)) {
            System.out.println("Bread munched!!!! +5 seconds");
            specialCounter += 1;
            addTimeCollected = true;
            return true;
        }
        return false;
    }

    /**
     * Checks if the player's current location is within the decreaseTime Rectangle area.
     * Runs on every render() of map1Maze.java under the handleInput() function.
     *
     * @param playerPosition Vector of the player's current position
     * @return True if player collected the decrease time debuff, false otherwise
     */
    public boolean checkDecreaseTimeCollision(Vector2 playerPosition) {
        if (!decreaseTimeCollected && decreaseTimeArea != null && decreaseTimeArea.contains(playerPosition)) {
            System.out.println("Stepped in mud!!!! -5 seconds");
            specialCounter += 1;
            decreaseTimeCollected = true;
            return true;
        }
        return false;
    }

    public boolean checkRandomTeleportTriggered(Vector2 playerPosition) {
        if (!randomTeleportTriggered && randomTeleportArea != null && randomTeleportArea.contains(playerPosition)) {
            System.out.println("Time to teleport the player!");
            specialCounter += 1;
            randomTeleportTriggered = true;
            return true;
        }
        return false;
    }

    /**
     * Returns the number of special events that have been triggered
     *
     * @return The total count of special events triggered
     */
    public int getSpecialCounter() {
        return specialCounter;
    }

    /**
     * Returns whether the addTime powerup has been collected
     *
     * @return True if add time powerup was collected, false otherwise
     */
    public boolean isAddTimeCollected() {
        return addTimeCollected;
    }

    /**
     * Returns whether the decrease_time debuff has been collected
     *
     * @return True if decrease time debuff was collected, false otherwise
     */
    public boolean isDecreaseTimeCollected() {
        return decreaseTimeCollected;
    }


    /**
     * Returns whether the random teleport has been completed
     *
     * @return True if the random teleport has previously been done, false otherwise
     */
    public boolean hasRandomTeleportTriggered() {
        return randomTeleportTriggered;
    }
}
