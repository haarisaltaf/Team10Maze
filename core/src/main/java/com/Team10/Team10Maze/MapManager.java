package com.Team10.Team10Maze;

// tiled map
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MapManager {
    // Tiled
    // load map, create renderer, update camera and set renderer to view of camera, then render
    private TiledMap tiledMap1;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer wallsLayer;
    private TiledMapTileLayer groundLayer;
    private MapLayer specialsLayer;
    private int tileSize = 32;

    // Special locations that will be found
    private Rectangle goalArea;
    private Rectangle addTimeArea;
    private Rectangle decreaseTimeArea;
    private Rectangle randomTeleportArea;
    private Vector2 playerSpawnPosition;
    private Vector2 chaserSpawnPosition;

    /**
     * Initialises the map manager and loads the tiled map.
     */
    public MapManager() {
        loadTiledMap();
    }

    /**
     * Loads the tiled map, initialises the map renderer, extracts tile layers, and handles special objects
     */
    private void loadTiledMap() {
        tiledMap1 = new TmxMapLoader().load("maps/Team10Maze1.tmx");
        // Renderer, 1 tile = 32 pixels
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap1, 1f / tileSize);

        groundLayer = (TiledMapTileLayer) tiledMap1.getLayers().get("ground");
        wallsLayer = (TiledMapTileLayer) tiledMap1.getLayers().get("walls");

        // saves as class attributes (playerPosition, addTimeArea, goalArea)
        handleSpecials();
    }

    /**
     * Iterates over every object in specials layer then checks if they are a Rectangle then
     * checks what type they are and handles if they are player spawn, chaser spawn, goal area,
     * add time area, and decrease time area
     */
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
                    playerSpawnPosition = new Vector2(tileX, tileY);
                    System.out.println("Found player spawn at: " + playerSpawnPosition);
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
                case "random_teleport":
                    randomTeleportArea = new Rectangle(tileX, tileY, 2, 2);
                    System.out.println("Found random teleport at: (" + tileX + ", " + tileY + ")");
                    break;
                case "decrease_time" :
                    decreaseTimeArea = new Rectangle(tileX, tileY, 2, 2);
                    System.out.println("Found decreaseTimeArea at: (" + tileX + ", " + tileY + ")");
                    break;
                case "chaser_spawn" :
                    chaserSpawnPosition = new Vector2(tileX, tileY);
                    System.out.println("Found chaserPosition at: (" + tileX + ", " + tileY + ")");

            }
            // remember to add to checkSpecialTileCollision() to handle when playerPosition overlaps
        }
        // TODO: handle if areas aren't found or dont exist -- moreso just edge case
    }

    /**
     * Returns the tiled map renderer
     *
     * @return The map renderer used to render the tiled map
     */
    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    /**
     * Returns the walls tile layer from the tiled map
     *
     * @return The walls layer used for collision detection
     */
    public TiledMapTileLayer getWallsLayer() {
        return wallsLayer;
    }

    /**
     * Returns the ground tile layer from the tiled map
     *
     * @return The ground layer
     */
    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    /**
     * Returns the goalArea rectangle from the map
     *
     * @return Rectangle defining the goal area on the map
     */
    public Rectangle getGoalArea() {
        return goalArea;
    }

    /**
     * Returns the addTime powerup area rectangle from the map
     *
     * @return Rectangle defining the add time powerup area on the map
     */
    public Rectangle getAddTimeArea() {
        return addTimeArea;
    }

    /**
     * Returns the decrease_time debuff  rectangle from the map
     *
     * @return Rectangle defining the decrease time debuff area on the map
     */
    public Rectangle getDecreaseTimeArea() {
        return decreaseTimeArea;
    }

    /**
     * Returns the random teleport rectangle from the map 
     *
     * @return Rectangle defining the random teleport area on the map
     */
    public Rectangle getRandomTeleportArea() {
        return randomTeleportArea;
    }

    /**
     * Returns the player spawn position
     *
     * @return Vector of the player's starting position
     */
    public Vector2 getPlayerSpawnPosition() {
        return playerSpawnPosition;
    }

    /**
     * Returns the chaser spawn position parsed from the map
     *
     * @return Vector of the chaser's starting position
     */
    public Vector2 getChaserSpawnPosition() {
        return chaserSpawnPosition;
    }

    /**
     * Disposes the tiled map and renderer
     */
    public void dispose() {
        tiledMap1.dispose();
        mapRenderer.dispose();
    }
}
