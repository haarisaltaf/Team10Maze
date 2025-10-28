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

// TODO: PRIORITY -- convert to tiled

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
    private boolean powerupCollected = false;

    // Camera and rendering
    private OrthographicCamera cameraMap1;

    // Game state
    private float timeLeft = 60f;
    private int tileSize = 64;

    public map1Maze(mazeGame game) {
        this.game = game;
        addTime(60f);
    }

    @Override public void show() {
        // init rendering
        batch = new SpriteBatch();
        font = new BitmapFont();
        // font.getData.setScale(2f);

        loadTiledMap();
        System.out.println("shouldve handled specials");

        cameraMap1 = new OrthographicCamera();
        // y-axis increasing towards top of screen, shows 18x18
        cameraMap1.setToOrtho(false, 18, 18);
    }

    private void loadTiledMap() {
        // load map1.tmx
        tiledMap1 = new TmxMapLoader().load("maps/Team10Maze1.tmx");

        // Renderer, 1 tile = 64 pixels
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap1, 1f / tileSize);

        // get Tile Layers from tileMap -- set as: walls, specials, ground
        groundLayer = (TiledMapTileLayer) tiledMap1.getLayers().get("ground");
        wallsLayer = (TiledMapTileLayer) tiledMap1.getLayers().get("walls");

        // will need to handle special types
        // saved as class attributes (playerPosition, addTimeArea, goalArea)
        handleSpecials();
        System.out.println("in loadTiledMap but after specials shouldbe handled");
    }

    public void handleSpecials() {
        System.out.println("before continue");
        MapLayer specialsLayer = tiledMap1.getLayers().get("specials");
        MapObjects specials = specialsLayer.getObjects();

        specials.forEach(specialEach -> System.out.println(specialEach));
        // check the type property (goal, add_time, player_spawn)
        for (MapObject special : specialsLayer.getObjects()) {
            // in the map we have only set the rectangle to have type
            // property so can easily skip non-rectangles
            if (!(special instanceof RectangleMapObject)) continue;
            System.out.println("after continue");


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
        // TODO: handle if areas aren't found or dont exist
    }



    @Override public void dispose() {
        font.dispose();
        batch.dispose();
    }

    @Override
    public void render(float delta) {
        cameraMap1.update();

        mapRenderer.setView(cameraMap1);
        mapRenderer.render();

    }

    public void addTime(float extraTime) {
        System.out.println("adding " + extraTime + " seconds");
        timeLeft += extraTime;
    }

    public void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            System.out.println("W");
            moveCharacter(0, -1); // moved up
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            System.out.println("A");
            moveCharacter(-1, 0); // moved left
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            System.out.println("S");
            moveCharacter(0, 1); // moved down
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            System.out.println("D");
            moveCharacter(1, 0); // moved right
        }

    }

    public void moveCharacter(int xDifference, int yDifference){
        System.out.println("moving nowhting");
    }

    public void goalReached() {

        System.out.println("CONGRATULTATIOANS");
        game.setScreen(new mainMenu(game));
        dispose();

    }

    @Override public void hide() { }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
}
