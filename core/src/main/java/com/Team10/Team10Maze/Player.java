package com.Team10.Team10Maze;

// libgdx imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Player {
    // init players' sprite, sprite, and position
    private Vector2 playerPosition;
    private TextureRegion playerSprite;
    private Texture playerTexture;
    private TiledMapTileLayer wallsLayer;

    public Player(Vector2 startPosition, TiledMapTileLayer wallsLayer) {
        this.playerPosition = startPosition;
        this.wallsLayer = wallsLayer;
        loadPlayerSprite();
    }

    private void loadPlayerSprite() {
        playerTexture = new Texture("SGQ_Dungeon/characters/main/elf.png");
        // TODO: change sprite to face correct direction of movement
        playerSprite = new TextureRegion(playerTexture, 0, 0, 16, 16);
        System.out.println("Player sprite loaded from tileset");
    }

    public Vector2 getPosition() {
        return playerPosition;
    }

    public void moveCharacter(int xDifference, int yDifference) {
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
    }

    public void render(SpriteBatch batch) {
        // sprite has size of 1x1
        batch.draw(playerSprite, playerPosition.x, playerPosition.y, 1f, 1f);
    }

    public void dispose() {
        playerTexture.dispose();
    }
}
