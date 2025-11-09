package com.Team10.Team10Maze;

public class GameTimer {
    private float timeLeft = 300; // 5 mins


    /**
     * Initalises the timer used in the game
     * 
     * @param startTime The time that the timer will start from
     */
    public GameTimer(float startTime) {
        this.timeLeft = startTime;
    }

    
    /**
     * Updates the time left using the passed parameter
     * 
     * @param delta Time passed in seconds since last frame was rendered based on the game FPS
     */
    public void update(float delta) {
        // updating timer
        timeLeft -= delta;
    }

    /**
     * Adds to the timer based on the parameter provided
     * 
     * @param extraTime Time to add to the timer
     */
    public void addTime(float extraTime) {
        System.out.println("adding " + extraTime + " seconds");
        timeLeft += extraTime;
    }


    /**
     * Adds to the timer based on the parameter provided
     * 
     * @param timeToDecrease Time to add to the timer
     */
    public void decreaseTime(float timeToDecrease) {
        System.out.println("removing " + timeToDecrease + " seconds");
        timeLeft -= timeToDecrease;
    }

    /**
     * Returns the amount of time left
     * 
     * @return The time left on the timer
     */
    public float getTimeLeft() {
        return timeLeft;
    }

    /**
     * Returns a boolean value based on if the timer is up or not
     * 
     * @return True if the timer has less than or equal to 0 seconds, false if otherwise
     */

    public boolean isTimeUp() {
        return timeLeft <= 0f;
    }
}
