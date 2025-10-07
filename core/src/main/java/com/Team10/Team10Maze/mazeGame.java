package com.Team10.Team10Maze;


import com.badlogic.gdx.ApplicationAdapter; // application window class
import com.badlogic.gdx.Gdx; // gives direct edits to application window
import com.badlogic.gdx.graphics.GL20; // controls openGL drawing
import com.badlogic.gdx.graphics.Texture; // image object class -- draws .jpeg/ .png
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // how gdx draws 2d images efficently
import com.badlogic.gdx.utils.ScreenUtils; // useful library for screen tasks eg clearing screen

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class mazeGame extends ApplicationAdapter { // inheriting Application class
    private SpriteBatch batch; // new drawing object
    private Texture image; // image object

    @Override
    public void create() {
        batch = new SpriteBatch(); // new creating batch image
        image = new Texture("libgdx.png");
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // clears image
        batch.begin(); // draws the image
        batch.draw(image, 140, 210);
        batch.end();
    }

    @Override
    public void dispose() {
// cleanup on exit (from gpu) as garbage collector doesnt work on gpu (applies to memory)
        batch.dispose();
        image.dispose();
    }
}

// TODO: add main menu screen -- buttons and background.jpeg
