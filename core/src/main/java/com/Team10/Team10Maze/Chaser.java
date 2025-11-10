package com.Team10.Team10Maze;

// libgdx imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

// java imports
import java.util.Random;

public class Chaser {
    private Vector2 position;
    private TiledMapTileLayer walls;
    private TextureRegion sprite;
    private Texture texture;

    private float moveTimer = 0f; // tracking time moving to limit how often to move
    private static final float MOVE_INTERVAL = 0.5f; // moves every half second
    private static final float CHASE_RANGE = 10f; // when within 10 tiles starts chasing directly
    private Random random = new Random();


    /**
     * Initalises the chaser
     * 
     * @param x Starting x coordinate of the chaser
     * @param y Starting y coordinate of the chaser
     * @param wallsLayer The wall layer on the map to ensure the chaser doesn't run into the wall
     */
    public Chaser(float x, float y, TiledMapTileLayer wallsLayer) {
        this.position = new Vector2(x, y);
        this.walls = wallsLayer;
        loadSprite();
    }


    /**
     * Moves chaser if the required time before new movements has been passed
     * 
     * @param delta Internal timer of LibGDX. Used to determine when the chaser should move
     * @param playerPosition Vector of the player's position
     */
    public void update(float delta, Vector2 playerPosition) {
        // need to pass-through player's current position
        moveTimer += delta;

        if (moveTimer >= MOVE_INTERVAL) {
            moveTimer = 0f;

            float distance = position.dst(playerPosition);

            if (distance < CHASE_RANGE) {
                chasePlayer(playerPosition);
            } else {
                randomMovement();
            }
        }
    }

    /**
     * Loads the sprite used for the chaser
     */
    private void loadSprite() {
        texture = new Texture("SGQ_Dungeon/characters/enemies/rat.png");
        sprite = new TextureRegion(texture, 0, 0, 16, 16);
    }

    /**
     * Returns the chaser's current position as a Vector2
     * 
     * @return Chaser's position
     */

    public Vector2 getPosition() {
        return position;
    }

    /**
     * Determines a random direction for the chaser to move in. 
     */
    private void randomMovement() {
        int nextDirection = random.nextInt(4);

        int dx = 0;
        int dy = 0;

        switch (nextDirection) {
            case 0:
                dy = 1;
                break;   // up

            case 1:
                dx = 1;
                break;   // right

            case 2:
                dy = -1;
                break;  // down

            case 3:
                dx = -1;
                break;  // left
        }
        attemptMove(dx, dy);
    }

    /**
     * Moves chaser towards the player based on their coordinates
     * 
     * @param playerPosition Vector of the player's position
     */
    private void chasePlayer(Vector2 playerPosition) {
        float dx = playerPosition.x - position.x;
        float dy = playerPosition.y - position.y;

        // checking if to move vert/ horizontally -- is player closer in y or x axis
        if (Math.abs(dx) > Math.abs(dy)) {
            attemptMove(dx > 0 ? 1 : -1, 0); // move horizontally
            // ternary operator : if dx > 0, return 1 else -1 -- if we move left or right
        } else {
            attemptMove(0, dy > 0 ? 1 : -1); // move vertically
        }
    }


    /**
     * 
     * Attempts to move the chaser to a coordinate determined by the difference in the provided X and Y coordinate
     * 
     * @param dx Attempted difference in the X coordinate
     * @param dy Attempted difference in the Y coordinate
     */
    private void attemptMove(int dx, int dy) {
        float newX = position.x + dx;
        float newY = position.y + dy;
        TiledMapTileLayer.Cell wallCell = this.walls.getCell((int)newX, (int)newY);

        // check if out of bounds
        if ( (newX < 0 || newX >= this.walls.getWidth()) ||
             (newY < 0 || newY >= this.walls.getHeight()) ) {
            return;
        }

        // Check collision with walls layer
        else if (wallCell != null && wallCell.getTile() != null) {
            return;
        }

        else {
            position.set(newX, newY);
        }
    }

    /**
     * 
     * @param batch Rendering tool to render the Chaser's sprite
     */
    public void render(SpriteBatch batch) {
        batch.draw(sprite, position.x, position.y, 1f, 1f);
    }


    /**
     * Disposes of the Chaser's texture
     */
    public void dispose() {
        texture.dispose();
    }
}
