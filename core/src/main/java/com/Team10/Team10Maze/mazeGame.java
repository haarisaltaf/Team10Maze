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

    @Override
    public void create() {
        batch = new SpriteBatch(); // new creating batch image

        // hold the grounds texture to then extract the region with the menuBackground
        dungeonGroundsTexture = new Texture("SGQ_Dungeon/grounds_and_walls/grounds.png");
        menuBackground = new TextureRegion(dungeonGroundsTexture, 64, 256, 64, 64); // at (64, 256) on ground.png take a region of 64x64
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // clears image
        batch.begin();
        batch.draw(menuBackground, 64, 64);  // draws the image
        batch.end();
    }

    @Override
    public void dispose() {
// cleanup on exit (from gpu) as garbage collector doesnt work on gpu (applies to memory)
        batch.dispose();
        dungeonGroundsTexture.dispose();
    }
}

// TODO: add main menu screen -- buttons and background.jpeg
