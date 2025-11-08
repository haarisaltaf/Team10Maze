package com.Team10.Team10Maze;

public class GameTimer {
    private float timeLeft = 300; // 5 mins

    public GameTimer(float startTime) {
        this.timeLeft = startTime;
    }

    public void update(float delta) {
        // updating timer
        timeLeft -= delta;
    }

    public void addTime(float extraTime) {
        System.out.println("adding " + extraTime + " seconds");
        timeLeft += extraTime;
    }

    public void decreaseTime(float extraTime) {
        System.out.println("removing " + extraTime + " seconds");
        timeLeft -= extraTime;
    }

    public float getTimeLeft() {
        return timeLeft;
    }

    public boolean isTimeUp() {
        return timeLeft <= 0f;
    }
}
