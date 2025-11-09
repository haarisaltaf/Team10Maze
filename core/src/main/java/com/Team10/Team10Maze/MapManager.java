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
    private int tileSize = 32;

    // Special locations that will be found
    private Rectangle goalArea;
    private Rectangle addTimeArea;
    private Rectangle decreaseTimeArea;
    private Vector2 playerSpawnPosition;
    private Vector2 chaserSpawnPosition;

    public MapManager() {
        loadTiledMap();
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
        for (MapObject special : specials) {
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
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public TiledMapTileLayer getWallsLayer() {
        return wallsLayer;
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    public Rectangle getGoalArea() {
        return goalArea;
    }

    public Rectangle getAddTimeArea() {
        return addTimeArea;
    }

    public Rectangle getDecreaseTimeArea() {
        return decreaseTimeArea;
    }

    public Vector2 getPlayerSpawnPosition() {
        return playerSpawnPosition;
    }

    public Vector2 getChaserSpawnPosition() {
        return chaserSpawnPosition;
    }

    public void dispose() {
        tiledMap1.dispose();
        mapRenderer.dispose();
    }
}
