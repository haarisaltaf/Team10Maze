package com.Team10.Team10Maze;

// Drawing, screen, and texture imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20; // controls openGL drawing
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

// input handling
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.Input;

// Application and Window class imports
import com.badlogic.gdx.utils.ScreenUtils; // useful library for screen tasks eg clearing screen

// Algorithm-based/ datatype imports
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.StringBuilder;


public class map1Maze implements Screen {
    private final mazeGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private float timeLeft = 3f;
    private int maxX = 33;
    private int maxY = 31;

    // -------- TEST MAP FOR NOW -------- allows for us to sort out the movement and display for now whilst waiting for proper maze to be done then can replace
    // KEY: "." = open path, "#" = wall, "G" for goal
    ArrayList<String> map =  new ArrayList<String> (Arrays.asList( //33x31
        "#################################",
        "#.G...........#...............###",
        "#.######.#####.#.#######.######.#",
        "#.#....#.....#.#.#.....#.#....#.#",
        "#.#.##.#####.#.#.#.###.#.#.##.#.#",
        "#...#......#.#.#...#.#.#.#....#.#",
        "#####.###.#...#####.#.#.#######.#",
        "#.....#.#.#.#.....#.#.#.....#...#",
        "#.#####.#.#.#####...#.#####.#.#.#",
        "#.#.....#.#.....#.#.#.....#.#.#.#",
        "#.#.#####.#####.#.#.#####.#.#.#.#",
        "#.#.#.........#.#.#.#.....#.#.#.#",
        "#.#.#.#########.#.#.#.#####.#.#.#",
        "#.#.#...........#.#.#.....#.#.#.#",
        "#.#.###########.#.#.#####.#.#.#.#",
        "#.#.............#.#.....#.#.#.#.#",
        "#.#############.#.#####.#.#.#.#.#",
        "#...............#...........#...#",
        "###########.#############.###.#.#",
        "#.........................#...#.#",
        "#.#######.###############.###.#.#",
        "#.......#.................#...#.#",
        "#.#####.###################.###.#",
        "#.#.........................#...#",
        "#.#.#.#####################.#.###",
        "#.#.#.....................#.#...#",
        "#.#.#####################.#.###.#",
        "#.#.......................#.....#",
        "#.############.....############.#",
        "#...............P...............#",
        "#################################"
    ));
    public int[] playerLocation = {16, 29}; // x, y
    public int[] goalLocation = {2, 1};


    public map1Maze(mazeGame game) {
        // passes through the superclass's (main Game) "this" instance

        batch = new SpriteBatch();
        font = new BitmapFont();

        this.game = game;
        addTime(60f);
    }

    @Override public void show() {
        map.forEach( (n) -> { System.out.println(n);} );
    }
    @Override public void dispose() {
        font.dispose();
        batch.dispose();
    }

    @Override
    public void render(float delta) {
        handleInput();
        timeLeft -= delta;

        if (timeLeft <= 0f) {
            game.setScreen(new mainMenu(game));
            dispose();
            return;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.getData().setScale(2f); // make text bigger
        font.draw(batch, "You’re in the Maze!", 100, 300);
        font.draw(batch, "Timer active: game will end in " + Math.max(0, (int)timeLeft) + "s", 100, 250);
        batch.end();
    }

    public void addTime(float extraTime) {
        System.out.println("adding " + extraTime + " seconds");
        timeLeft += extraTime;
    }

    public void handleInput() {
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            System.out.println("W");
            moveCharacter('W', 0, -1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            System.out.println("A");
            moveCharacter('A', -1, 0);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            System.out.println("S");
            moveCharacter('S',  0, 1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            System.out.println("D");
            moveCharacter('D', 1, 0);
        }

    }

    public void moveCharacter(char letter, int xDifference, int yDifference){
        // If G, then overlap and set as winner.
        if (yDifference != 0) {

            // Get the line above the player's current line
            String lineAbove = map.get(playerLocation[1]+yDifference);
            if (lineAbove == null) {
                // Return if that line doesn't exist
                return;
            }

            String currLine = map.get(playerLocation[1]);
            if (lineAbove.charAt(playerLocation[0]) == '#'){
                // Return if they are trying to go through a wall
                return;
            }

            // Replace the P with a . and set the current line without the P
            currLine = currLine.replace('P', '.');
            map.set(playerLocation[1], currLine);

            // Replace the player's X coordinate with a P on their new Y coordinate
            StringBuilder newLineAbove = new StringBuilder(lineAbove);
            newLineAbove.setCharAt(playerLocation[0], 'P');
            map.set(playerLocation[1]+yDifference, newLineAbove.toString());

            playerLocation[1] += yDifference;
            if (playerLocation[0] == goalLocation[0] && playerLocation[1] == goalLocation[1]){
                goalReached();
                return;
            }

            map.forEach( (n) -> { System.out.println(n);} );
            return;
        }


        if (xDifference != 0) {

            // Get player's current line
            String currLine = map.get(playerLocation[1]);
            if (playerLocation[0]+xDifference > maxX || currLine.charAt(playerLocation[0]+xDifference) == '#') {
                // Checks if they are past the limit or going into a wall and returns if so
                return;
            }

            // Remove the P from their current line
            currLine = currLine.replace('P', '.');
            map.set(playerLocation[1], currLine);

            // Add the P into their new X coordinate 
            StringBuilder newCurrLine = new StringBuilder(currLine);
            newCurrLine.setCharAt(playerLocation[0]+xDifference, 'P');
            map.set(playerLocation[1], newCurrLine.toString());

            // ---- Updating current location of where player is
            playerLocation[0] += xDifference;
            if (playerLocation[0] == goalLocation[0] && playerLocation[1] == goalLocation[1]){
                goalReached();
                return;
            }

            map.forEach( (n) -> { System.out.println(n);} );
            return;
        }
    }

    public void goalReached() {

        System.out.println("CONGRATULTATIOANS");
        // game.setScreen(new mainMenu(game));
        // dispose();

    }

    @Override public void hide() { }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
}
