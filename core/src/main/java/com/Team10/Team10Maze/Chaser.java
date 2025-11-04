package com.Team10.Team10Maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Chaser {
    private Vector2 position;
    private TextureRegion sprite;
    private Texture texture;

    public Chaser(float x, float y) {
        this.position = new Vector2(x, y);
        loadSprite();
    }

    private void loadSprite() {
        texture = new Texture("SGQ_Dungeon/characters/main/rat.png");
        sprite = new TextureRegion(texture, 16, 0, 16, 16);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch batch) {
        batch.draw(sprite, position.x, position.y, 1f, 1f);
    }

    public void dispose() {
        texture.dispose();
    }
}
