package com.Team10.Team10Maze;


// Application and Window class imports
import com.badlogic.gdx.ApplicationAdapter; // application window class
import com.badlogic.gdx.Gdx; // gives direct edits to application window
import com.badlogic.gdx.utils.ScreenUtils; // useful library for screen tasks eg clearing screen

// Drawing and texture imports
import com.badlogic.gdx.graphics.GL20; // controls openGL drawing
import com.badlogic.gdx.graphics.Texture; // image object class -- draws .jpeg/ .png
import com.badlogic.gdx.graphics.g2d.TextureRegion; // Allows for subsection of a jpeg to be taken
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // how gdx draws 2d images efficently

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class mazeGame extends ApplicationAdapter { // inheriting Application class
    private SpriteBatch batch; // new drawing object
    private Texture dungeonGroundsTexture;
    private TextureRegion menuBackground; // image object
    private float[] lengthxy;
    // TODO: change to TextureRegion[][] ... = TextureRegion.split() to auto split the entire image evenly into tiles - sort out once more than just the background is needed


    @Override
    public void create() {
        batch = new SpriteBatch(); // new creating batch image

        // hold the grounds texture to then extract the region with the menuBackground
        dungeonGroundsTexture = new Texture("SGQ_Dungeon/grounds_and_walls/grounds.png");
        menuBackground = new TextureRegion(dungeonGroundsTexture, 64, 192, 64, 64); // at (64, 256) on ground.png take a region of 64x64
        lengthxy = getBackgroundSize();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // clears image
        batch.begin();
        // batch.draw(wallRegion, x, y, smallest dimenstion, smallest dimenstion)
        batch.draw(menuBackground, lengthxy[1], lengthxy[2], lengthxy[0], lengthxy[0]);  // draws the image
        batch.end();
    }

    @Override
    public void dispose() {
// cleanup on exit (from gpu) as garbage collector doesnt work on gpu (applies to memory)
        batch.dispose();
        dungeonGroundsTexture.dispose();
    }

    public float[] getBackgroundSize() {
        // TODO: making a function that can calculate the correct size of the square background image.

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // calc max-length to strech without overscanning, use smallest dimension:
        float length = Math.min(screenWidth, screenHeight);
        float x = (screenWidth - length) / 2f;
        float y = (screenHeight - length) / 2f;

        float[] returnValue = {length, x, y};
        return returnValue;
    }
}

// TODO: add main menu screen -- buttons and background.jpeg
