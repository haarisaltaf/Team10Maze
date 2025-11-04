package com.Team10.Team10Maze;

// libgdx imports
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMapLayer;

// java imports
import java.util.Random;

public class Chaser {
    private Vector2 position;
    private TextureRegion sprite;
    private Texture texture;

    private float moveTimer = 0f; // tracking time moving to limit how often to move
    private static final float MOVE_INTERVAL = 0.5f; // moves every half second
    private static final float CHASE_RANGE = 5f; // moves every half second
    private Random random = new Random();


    // passing through the walls layer as this stays constant and would
    // allow for us to grab width/ height of map and walls themselves
    // so can figure out where OOB and walls are to stop movement in that direction
    public Chaser(float x, float y, TiledMapLayer wallsLayer) {
        this.position = new Vector2(x, y);
        this.walls = wallsLayer;
        loadSprite();
    }


    private void update(float delta, Vector2 playerPosition) {
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

            System.out.println("Chaser moved again, 0.5sec till next");
        }
    }

    private void loadSprite() {
        texture = new Texture("SGQ_Dungeon/characters/enemies/rat.png");
        sprite = new TextureRegion(texture, 16, 0, 16, 16);
    }

    public Vector2 getPosition() {
        return position;
    }

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
        attemptMove(dx, dy, this.walls);
    }

    private void chasePlayer(Vector2 playerPosition) {
        float dx = playerPosition.x - position.x;
        float dy = playerPosition.y - position.y;

        // checking if to move vert/ horizontally
        if (Math.abs(dx) > Math.abs(dy)) {
            attemptMove(dx > 0 ? 1 : -1, 0, map); // move horizontally
        } else {
            attemptMove(0, dy > 0 ? 1 : -1, map); // move vertically
        }
    }

    private void attemptMove(int dx, int dt) {
        float newX = position.x + dx;
        float newY = position.y + dy;
        TiledMapTileLayer.Cell wallCell = this.walls.getCell((int)newX, (int)newY);

        // check if out of bounds
        if ( (newX < 0 || newX >= this.walls.getWidth()) ||
             (newY < 0 || newY >= this.walls.getHeight()) ) {
            System.out.println("chaser movement blocked - out of bounds");
            return;
        }

        // Check collision with walls layer
        else if (wallCell != null && wallCell.getTile() != null) {
            System.out.println("chaser movement blocked - wall");
        }

        else {
            position.set(newX, newY);
            System.out.println("chaser moved to: " + position);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(sprite, position.x, position.y, 1f, 1f);
    }

    public void dispose() {
        texture.dispose();
    }
}
