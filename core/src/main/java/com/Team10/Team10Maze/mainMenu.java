package com.Team10.Team10Maze;


// Drawing and texture imports
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20; // controls openGL drawing
import com.badlogic.gdx.graphics.Texture; // image object class -- draws .jpeg/ .png
import com.badlogic.gdx.graphics.g2d.TextureRegion; // Allows for subsection of a jpeg to be taken
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // how gdx draws 2d images efficently
import com.badlogic.gdx.scenes.scene2d.ui.Button; // import button

// Application and Window class imports
import com.badlogic.gdx.Gdx; // gives direct edits to application window
import com.badlogic.gdx.utils.ScreenUtils; // useful library for screen tasks eg clearing screen


public class mainMenu implements Screen {
    private final mazeGame game;
    private SpriteBatch batch; // new drawing object
    private Texture dungeonGroundsTexture;
    private TextureRegion menuBackground; // image object
    private float[] lengthxy;
    // TODO: change to TextureRegion[][] ... = TextureRegion.split() to auto split the entire image evenly into tiles - sort out once more than just the background is needed


    public mainMenu(mazeGame game) {
        // passes through the superclass's "this" instance
        this.game = game;

    }

    @Override
    public void show() {
        batch = new SpriteBatch(); // new creating batch image

        dungeonGroundsTexture = new Texture("SGQ_Dungeon/grounds_and_walls/grounds.png");
        menuBackground = new TextureRegion(dungeonGroundsTexture, 64, 192, 64, 64); // at (64, 192) on ground.png take a region of 64x64
        lengthxy = getBackgroundSize();
    }

    @Override
    public void render(float delta) { // needs delta to have the time since previous frame -- smoothness
        // ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f); // clears mage
        batch.begin();
        // batch.draw(wallRegion, x, y, smallest dimenstion, smallest dimenstion)
        batch.draw(menuBackground, lengthxy[1], lengthxy[2], lengthxy[0], lengthxy[0]);  // draws the image
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        dungeonGroundsTexture.dispose();
    }

    @Override public void hide() { }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

    public float[] getBackgroundSize() {

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

// TODO: add main menu screen -- ****buttons || DONE: background.jpeg
