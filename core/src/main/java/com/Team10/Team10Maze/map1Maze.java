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

    // -------- TEST MAP FOR NOW -------- allows for us to sort out the movement and display for now whilst waiting for proper maze to be done then can replace
    // KEY: "." = open path, "#" = wall, "G" for goal
    ArrayList<String> map =  new ArrayList<String> (Arrays.asList( //33x31
        "#################################",
        "#.G...........#...............###",
        "#.######.#####.#.#######.######.#",
        "#.#....#.....#.#.#.....#.#....#.#",
        "#.#.##.#####.#.#.#.###.#.#.##.#.#",
        "#...##.....#.#.#...#.#.#.#....#.#",
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
        "#.........#...............#...#.#",
        "#.#######.###############.###.#.#",
        "#.......#.................#...#.#",
        "#.#####.###################.###.#",
        "#.#...#.....................#...#",
        "#.#.#.#####################.#.###",
        "#.#.#.....................#.#...#",
        "#.#.#####################.#.###.#",
        "#.#.......................#.....#",
        "#.#############################.#",
        "#...............P...............#",
        "#################################"
    ));
    public int[] playerLocation = {16, 29}; // x, y
    public int[] goalLocation = {2, 1}


    public map1Maze(mazeGame game) {
        // passes through the superclass's (main Game) "this" instance

        batch = new SpriteBatch();
        font = new BitmapFont();

        this.game = game;
        addTime(5f);
    }

    @Override public void show() { }
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

    // TODO: CHECK ALL OF THESE INPUTS AND HANDLE THEM CORRECTLY -- Refer to W as this is done relatively ok for now: few edge cases may be funky
    // Also need to handle when reaching goal -- store where goal is then check if playerLocation == goalLocation every movement?
    public void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            System.out.println("W");
            moveCharacter('W');
            map.forEach( (n) -> { System.out.println(n);} );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            System.out.println("A");
            if (playerLocation[0] <= 0) { // TODO: move to within moveCharacter method like when handling W
                return;
            }
            moveCharacter('A');
            playerLocation[0] -= 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            System.out.println("S");
            if (playerLocation[1] >= 31) {
                return;
            }
            moveCharacter('S');
            playerLocation[1] += 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            System.out.println("D");
            if (playerLocation[0] >= 32) {
                return;
            }
            moveCharacter('D');
            playerLocation[0] += 1;
        }

    }

    public void moveCharacter(char letter){
        // letter == direction to move, checks if wall there and returns the same if so.
        // If no wall, then move the player in that direction and return new map.
        // If G, then overlap and set as winner.
        if (letter == 'W') {
            String lineAbove = map.get(playerLocation[1]-1);
            String currLine = map.get(playerLocation[1]);

            // ----- dont move if wall -----
            if (lineAbove.charAt(playerLocation[0]) == '#' || playerLocation[1] <= 0){
                return;
            }

            // ----- format currLine -----
            currLine = currLine.replace('P', '.');
            map.set(playerLocation[1], currLine);


            // ----- format lineAbove -----
            StringBuilder newLineAbove = new StringBuilder(lineAbove);
            newLineAbove.setCharAt(playerLocation[0], 'P');
            map.set(playerLocation[1]-1, newLineAbove.toString());

            // ---- Updating current location of where player is
            playerLocation[1] -= 1;
            if (playerLocation == goalLocation){
                goalReached;
            }
        }

        // TODO: handle movement for A and S and D
        if (letter == 'A') {

        }
        if (letter == 'S') {

        }
        if (letter == 'D') {

        }
    }

    public void goalReached() {

        System.out.println("CONGRATULTATIOANS");

    }

    @Override public void hide() { }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
}
