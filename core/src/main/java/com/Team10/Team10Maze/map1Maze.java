package com.Team10.Team10Maze;

// Drawing, screen, and texture imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20; // controls openGL drawing
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// input handling
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.OrthographicCamera;

// text
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;

public class map1Maze implements Screen {
    // main inits
    private final mazeGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    // adding other class files
    private MapManager mapManager;
    private Player player;
    private GameTimer gameTimer;
    private SpecialTiles specialTiles;
    private Chaser chaser;

    // Camera and rendering
    private OrthographicCamera cameraMap1;

    // Game state
    private boolean gameEnding = false; // flag to prevent crash when getting goal
    private boolean gamePaused = false;

    // checks if gameending == True then goes to main menu as finished

    /**
     * Initalises the first map of the maze
     *
     * @param game The game instance that allows us to change the screen
     */
    public map1Maze(mazeGame game) {
        this.game = game;
    }


    /**
     * Initalises the camera, timer, map tiles and chaser
     */
    @Override public void show() {
        // init rendering
        batch = new SpriteBatch();
        font = new BitmapFont();
        // font.getData.setScale(2f);

        mapManager = new MapManager();

        cameraMap1 = new OrthographicCamera();
        // y-axis increasing towards top of screen, shows 18x18
        cameraMap1.setToOrtho(false, 18, 18);

        // initialize player with spawn position from map
        player = new Player(mapManager.getPlayerCurrentPosition(), mapManager.getWallsLayer());

        gameTimer = new GameTimer(300); // 5 mins

        // initialize special tiles with areas from map
        specialTiles = new SpecialTiles(
            mapManager.getGoalArea(),
            mapManager.getAddTimeArea(),
            mapManager.getDecreaseTimeArea(),
            mapManager.getRandomTeleportArea()
        );

        // initialize chaser
        chaser = new Chaser(
            mapManager.getChaserSpawnPosition().x,
            mapManager.getChaserSpawnPosition().y,
            mapManager.getWallsLayer()
        );

        updateCamera();
    }

    /**
     * Updates the camera position ensuring it doesn't show any out of bounds area
     */
    private void updateCamera() {
        //setting camera to follow playerPosition + half a coord, overhead of 0 as orthographic camera so depth dont matter
        cameraMap1.position.set(player.getPosition().x + 0.5f, player.getPosition().y + 0.5f, 0);

        int mapWidth = mapManager.getWallsLayer().getWidth();
        int mapHeight = mapManager.getWallsLayer().getHeight();

        // now want to ensure the camera cant go OOB/ only show mainly the map itself
        // calc half of viewportWidth/ height
        // character position +- half of the viewport in x and y:w
        float halfViewportWidth = cameraMap1.viewportWidth / 2f;
        float halfViewportHeight = cameraMap1.viewportHeight / 2f;

        // halt camera's x position so viewport never shows past left or right edges
        cameraMap1.position.x = Math.max(halfViewportWidth,
            Math.min(mapWidth - halfViewportWidth, cameraMap1.position.x));

        // halt camera's y position so viewport never shows past top or bottom edges
        cameraMap1.position.y = Math.max(halfViewportHeight,
            Math.min(mapHeight - halfViewportHeight, cameraMap1.position.y));

        cameraMap1.update();
    }

    /**
     * Disposes of all previously used cameras, text, textures, chasers and anything map related
     */
    @Override public void dispose() {
        font.dispose();
        batch.dispose();
        mapManager.dispose();
        chaser.dispose();
        player.dispose();
    }


    /**
     * Renders the map. This method is also responsible for
     * <ul>
     *  <li>Handling player input</li>
     *  <li>Updating the timer value</li>
     *  <li>Updating the chaser's position</li>
     *  <li>Detecting chaser -> player collisions</li>
     *  <li>Detecting game ending conditions</li>
     *  <li>Rendering the UI</li>
     * </ul>
     *
     * @param delta Time passed in seconds since the last frame was rendered
     */
    @Override
    public void render(float delta) {
        // if gameEnding == true then endMap() SHOULD have already been called so can just return;
        if (gameEnding) {
            // clear the screen before transitioning to prevent showing map in background
            Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            return;
        }

        // update camera, clear screen, render camera
        handleInput();

        if (!gamePaused) {
            gameTimer.update(delta);
        }

        cameraMap1.update();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapManager.getMapRenderer().setView(cameraMap1);
        mapManager.getMapRenderer().render();

        // chaser movement update then check if collision
        chaser.update(delta, player.getPosition());
        // epsilon equals function fuzzy compares 2 vectors
        if (chaser.getPosition().epsilonEquals(player.getPosition(), 0.5f)){
            System.out.println("CAUGHT BY CHASER"); // TODO: HAVE TTHIS BE A GAMEOVER SCREEN
            endMap(false);
            return;
        }

        // drawing sprite using camera location
        batch.setProjectionMatrix(cameraMap1.combined);
        batch.begin();
        player.render(batch);
        chaser.render(batch);
        batch.end();

        // rendering on-screen ui
        renderUI();

        // exit to mainmenu if run out of time
        if (!gamePaused) {
            if (gameTimer.isTimeUp()) {
                System.out.println("Time's up! Game Over");
                endMap(false); // method to move back to main menu
                return;
            }
        }
    }

    /**
     * Renders the game UI. It does all these to do that:
     * <ul>
     *  <li>Shows text related to the game</li>
     *  <li>Shows specific text to a player if they are paused</li>
     * </ul>
     */
    private void renderUI() {
        // setting as a projection to the view itself so doesnt move with camera movement
        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();

        if (!gamePaused) {
            // adding timer
            font.getData().setScale(1f);
            String timerText = "Timer: " + Math.max(0, (int)gameTimer.getTimeLeft()) + "s";

            // draw timer
            font.draw(batch, timerText, 20, 60);

            // show on-screen controls
            font.draw(batch, "WASD to move", 20, 40);
            font.draw(batch, "Number of Events Triggered: " + specialTiles.getSpecialCounter(), 20, 20);

            // showing if addTime has been collected
            if (specialTiles.isAddTimeCollected()) {
                font.draw(batch, "Bread Munched!!!! +5 Seconds", 20, 80);
            }

            if (specialTiles.isDecreaseTimeCollected()) {
                font.draw(batch, "Stepped in Mud!!!! -5 Seconds", 20, 100);
            }

        } else {
            font.getData().setScale(3f);
            String pauseText = "GAME PAUSED!\\nTime remaining: " + Math.max(0, (int)gameTimer.getTimeLeft()) + "s\\nPress ESC to continue";
            GlyphLayout layout = new GlyphLayout(font, pauseText);

            float screenW = Gdx.graphics.getWidth();
            float screenH = Gdx.graphics.getHeight();
            float x = (screenW / 2f) - (layout.width / 2f);
            float y = (screenH / 1.3f) + (layout.height / 1.3f);

            font.setColor(Color.BLACK);
            font.draw(batch, layout, x + 2, y);
            font.draw(batch, layout, x - 2, y);
            font.draw(batch, layout, x, y + 2);
            font.draw(batch, layout, x, y - 2);

            font.setColor(Color.WHITE);
            font.draw(batch, layout, x, y);
        }

        batch.end();
    }

    /**
     * Handles player input
     *
     * <ul>
     *  <li>Uses user input to move the player</li>
     *  <li>Uses user input to pause/resume the game</li>
     * </ul>
     */
    public void handleInput() {
        if (!gamePaused) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                player.moveCharacter(0, 1);
                updateCamera();
                checkSpecialTileCollision();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                player.moveCharacter(-1, 0);
                updateCamera();
                checkSpecialTileCollision();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                player.moveCharacter(0, -1);
                updateCamera();
                checkSpecialTileCollision();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                player.moveCharacter(1, 0);
                updateCamera();
                checkSpecialTileCollision();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gamePaused = !gamePaused;
            System.out.println(gamePaused);
        }
    }

    /**
     * Called when player reaches the end coordinates of the map
     */
    public void goalReached() {
        endMap(true);
    }

    /**
     * Sets the user to the game over screen
     *
     * @param gameWon True if the player won the game, false if they lost
     */
    private void endMap(boolean gameWon) {
        gameEnding = true;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new gameOver(game, gameWon, gameTimer.getTimeLeft()));
                dispose();
            }
        });
    }

    /**
     * Checks for different types of collisions
     *
     * <ul>
     *  <li>If the player reached the end coordinates</li>
     *  <li>If the player reached the buff (adds 5seconds of time)</li>
     *  <li>If the player reached the debuff (removes 5 seconds of time)</li>
     * </ul>
     */
    private void checkSpecialTileCollision() {
        // Check goal first - end game
        if (specialTiles.isGoalReached(player.getPosition())) {
            goalReached();
            return;
        }

        // Check addTime
        if (specialTiles.checkAddTimeCollision(player.getPosition())) {
            gameTimer.addTime(5f);
        }



        // Check decreaseTime
        if (specialTiles.checkDecreaseTimeCollision(player.getPosition())) {
            gameTimer.decreaseTime(5f);
        }

        if (specialTiles.checkRandomTeleportTriggered(player.getPosition())) {
            System.out.println("Spawn: " + mapManager.getPlayerStartPosition());
            player.getPosition().set(mapManager.getPlayerStartPosition());
        }
    }

    @Override public void hide() { }
    @Override public void resize(int width, int height) {} // will handle resizing of app
    @Override public void pause() {}
    @Override public void resume() {}


    // OVERALL TODOS:
    // ASSESSMENT 1 NEEDS:
    // For Assessment 1, you are only required to implement one of each type of event (one negative, one positive and one hidden),
    // debuff: deen, buff: add_time, invisible: debuff --- MAKE THESE UNI_BASED
    // a tracker of the time the game lasts (up to 5 real-world minutes), -- DONE
    // a simple counter denoting how many of each event have been interacted with. -- DONE
    //
    //
    // cleanup assets -- Map cleanup and sprite cleanup
    // add VISIBLE GOAL AND POWERUP ON THE MAP
    // add more powerups -- look at addTime and change a bit
}
