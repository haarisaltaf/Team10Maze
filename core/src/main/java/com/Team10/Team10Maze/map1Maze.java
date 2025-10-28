package com.Team10.Team10Maze;

// Drawing, screen, and texture imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20; // controls openGL drawing
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.Stage;

// input handling
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.Input;

// Application and Window class imports
import com.badlogic.gdx.utils.ScreenUtils; // useful library for screen tasks eg clearing screen

// Algorithm-based/ datatype imports
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.StringBuilder;

// tiled map
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// player
import com.badlogic.gdx.math.Vector2;

public class map1Maze implements Screen {
    // main inits
    private final mazeGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;

    // Tiled
    // load map, create renderer, update camera and set renderer to view of camera, then render
    private TiledMap tiledMap1;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer wallsLayer;
    private TiledMapTileLayer groundLayer;
    private MapLayer specialsLayer;

    // Player
    // init players' sprite, sprite, and position
    private Vector2 playerPosition;
    private TextureRegion playerSprite;
    private Texture playerTexture;

    // Special locations
    private Rectangle goalArea;
    private Rectangle addTimeArea;
    private boolean addTimeCollected = false;

    // Camera and rendering
    private OrthographicCamera cameraMap1;

    // Game state
    private float timeLeft = 60f;
    private int tileSize = 32;
    private boolean gameEnding = false; // flag to prevent crash when getting goal
    // checks if gameending == True then goes to main menu as finished


    public map1Maze(mazeGame game) {
        this.game = game;
    }

    @Override public void show() {
        // init rendering
        batch = new SpriteBatch();
        font = new BitmapFont();
        // font.getData.setScale(2f);

        loadTiledMap();

        cameraMap1 = new OrthographicCamera();
        // y-axis increasing towards top of screen, shows 18x18
        cameraMap1.setToOrtho(false, 18, 18);

        loadPlayerSprite();

        updateCamera();
    }

    private void loadPlayerSprite() {

        playerTexture = new Texture("SGQ_Dungeon/characters/main/elf.png");

        // TODO: change sprite to face correct direction of movement
        playerSprite = new TextureRegion(playerTexture, 0, 0, 16, 16);
        System.out.println("Player sprite loaded from tileset");

    }

    private void updateCamera() {

        //setting camera to follow playerPosition + half a coord, overhead of 0 as orthographic camera so depth dont matterereerer
        cameraMap1.position.set(playerPosition.x + 0.5f, playerPosition.y + 0.5f, 0);

        int mapWidth = wallsLayer.getWidth();
        int mapHeight = wallsLayer.getHeight();

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

    private void loadTiledMap() {
        // load map1.tmx
        tiledMap1 = new TmxMapLoader().load("maps/Team10Maze1.tmx");

        // Renderer, 1 tile = 32 pixels
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap1, 1f / tileSize);

        // get Tile Layers from tileMap -- set as: walls, specials, ground
        groundLayer = (TiledMapTileLayer) tiledMap1.getLayers().get("ground");
        wallsLayer = (TiledMapTileLayer) tiledMap1.getLayers().get("walls");

        // will need to handle special types
        // saved as class attributes (playerPosition, addTimeArea, goalArea)
        handleSpecials();
    }

    public void handleSpecials() {
        MapLayer specialsLayer = tiledMap1.getLayers().get("specials");
        MapObjects specials = specialsLayer.getObjects();

        // check the type property (goal, add_time, player_spawn)
        for (MapObject special : specialsLayer.getObjects()) {
            // in the map we have only set the rectangle to have type
            // property so can easily skip non-rectangles
            if (!(special instanceof RectangleMapObject)) continue;


            // get rectangle map object, extravt the rectangle,
            // then grab properties of rectangle
            RectangleMapObject rectangleObject = (RectangleMapObject) special;
            MapProperties rectProperties = rectangleObject.getProperties();

            // grab type property
            String type = rectProperties.get("type", String.class);
            if (type == null) continue;

            // pixel to tile coord conversion
            Rectangle currRectangle = rectangleObject.getRectangle();
            float tileX = currRectangle.getX() / tileSize;
            float tileY = currRectangle.getY() / tileSize;

            // switch case to check if player_spawn/ goal/ add_time
            // stores location of each to then check if overlap ever occurs
            System.out.println("checking type");
            switch (type) {
                case "player_spawn" :
                    playerPosition = new Vector2(tileX, tileY);
                    System.out.println("Found player spawn at: " + playerPosition);
                    break;

                case "add_time" :
                // on map, areas have been drawn as 2x2 areas
                    addTimeArea = new Rectangle(tileX, tileY, 2, 2);
                    System.out.println("Found addTime at: (" + tileX + ", " + tileY + ")");
                    break;

                case "goal" :
                    goalArea = new Rectangle(tileX, tileY, 2, 2);
                    System.out.println("Found goalArea at: (" + tileX + ", " + tileY + ")");
                    break;
            }
        }
        // TODO: handle if areas aren't found or dont exist -- moreso just edge case
    }



    @Override public void dispose() {
        font.dispose();
        batch.dispose();
        tiledMap1.dispose();
        mapRenderer.dispose();
        playerTexture.dispose();
    }

    @Override
    public void render(float delta) {
        // if gameEnding == true then endMap() has already been called so can just return;
        if (gameEnding) {
            return;
        }

        // update camera, clear screen, render camera
        handleInput();

        // updating timer
        timeLeft -= delta;

        cameraMap1.update();

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(cameraMap1);
        mapRenderer.render();

        // drawing sprite using camera location
        batch.setProjectionMatrix(cameraMap1.combined);
        batch.begin();
        // sprite has size of 1x1
        batch.draw(playerSprite, playerPosition.x, playerPosition.y, 1f, 1f);
        batch.end();

        // rendering on-screen ui
        renderUI();

        // exit to mainmenu if run out of time
        if (timeLeft <= 0f) {
            System.out.println("Time's up! Game Over");
            endMap(); // method to move back to main menu
            return;
        }

    }


    private void renderUI() {
        // setting as a projection to the view itself so doesnt move with camera movement
        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();

        // adding timer
        String timerText = "Timer: " + Math.max(0, (int)timeLeft) + "s";

        // draw timer
        font.draw(batch, timerText, 20, 60);

        // show on-screen controls
        font.draw(batch, "WASD to move", 20, 40);

        // showing if addTime has been collected
        if (addTimeCollected) {
            font.draw(batch, "addTime collected!", 20, 80);
        }

        batch.end();
    }

    public void addTime(float extraTime) {
        System.out.println("adding " + extraTime + " seconds");
        timeLeft += extraTime;
    }

    public void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            moveCharacter(0, 1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            moveCharacter(-1, 0);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            moveCharacter(0, -1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            moveCharacter(1, 0);
        }
    }

    public void moveCharacter(int xDifference, int yDifference){
        // grab new location
        float newX = playerPosition.x + xDifference;
        float newY = playerPosition.y + yDifference;

        // check map bounds -- return if at edge
        if ((newX < 0 || newX >= wallsLayer.getWidth()) ||
            (newY < 0 || newY >= wallsLayer.getHeight())) {
            System.out.println("Movement blocked - out of bounds");
            return;
        }

        // Check collision with walls layer
        TiledMapTileLayer.Cell wallCell = wallsLayer.getCell((int)newX, (int)newY);
        if (wallCell != null && wallCell.getTile() != null) {
            System.out.println("Movement blocked - wall");
            return;
        }

        // movement is valid -- set playerPosition to new position then update camera
        playerPosition.set(newX, newY);
        System.out.println("player moved to: " + playerPosition);

        updateCamera();

        checkSpecialTileCollision();
    }

    public void goalReached() {
        // TODO: add congratulations scene
        System.out.println("CONGRATULTATIOANS");
        endMap();

    }

    private void endMap() {
        gameEnding = true;

        // setting new runnable stops crashing when reaching end goal
        // queues transition to mainMenu for NEXT frame so doesnt crash
        // when trying to load new screen on same frame
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new mainMenu(game));
                dispose();
            }
        });

    }

    private void checkSpecialTileCollision() {
        // Check goal first - end game
        if (goalArea != null && goalArea.contains(playerPosition)) {
            System.out.println("GOAL REACHED! You win!");
            goalReached();
            return;
        }

        // Check addTime
        if (!addTimeCollected && addTimeArea != null && addTimeArea.contains(playerPosition)) {
            System.out.println("addtime collected! +5 seconds");
            addTime(5f);
            addTimeCollected = true;
        }
    }
    @Override public void hide() { }
    @Override public void resize(int width, int height) {} // will handle resizing of app
    @Override public void pause() {}
    @Override public void resume() {}
}



// OVERALL TODOS:
//      cleanup assets -- Map cleanup and sprite cleanup
//      add VISIBLE GOAL AND POWERUP ON THE MAP
//      ADD CHASER
//      ADD DEBUFF -- look at addTime function and change a lil
//      add more powerups -- look at addTime and change a bit

    //      WHO CARES:
    //      Pause menu
    //      clear screen upon reaching goal properyl so no glitchy background
    //      HAVE IT WORK WHEN FULL SCREENED
