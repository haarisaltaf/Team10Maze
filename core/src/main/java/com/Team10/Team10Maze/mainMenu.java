package com.Team10.Team10Maze;


// drawing and texture imports
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20; // controls openGL drawing
import com.badlogic.gdx.graphics.Texture; // image object class -- draws .jpeg/ .png
import com.badlogic.gdx.graphics.g2d.TextureRegion; // allows for subsection of a jpeg to be taken
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // how gdx draws 2d images efficently

// stage, ui elements etc
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

// Application and Window class imports
import com.badlogic.gdx.Gdx; // gives direct edits to application window
import com.badlogic.gdx.utils.ScreenUtils; // useful library for screen tasks eg clearing screen


public class mainMenu implements Screen {
    private final mazeGame game;
    private SpriteBatch batch; // new drawing object
    private Texture dungeonGroundsTexture;
    private TextureRegion menuBackground; // image object
    private float[] lengthxy;
    private FitViewport viewport;
    private Stage stage;
    // TODO: change to TextureRegion[][] ... = TextureRegion.split() to auto split the entire image evenly into tiles - sort out once more than just the background is needed


    public mainMenu(mazeGame game) {
        // passes through the superclass's (main Game) "this" instance
        this.game = game;

    }

    @Override
    public void show() {
        // implementing skin and stage
         stage = new Stage(new FitViewport(640, 480));
        Gdx.input.setInputProcessor(stage);
        Skin buttonSkin = new Skin(Gdx.files.internal("uiskin.json"));


        // implementing button and click listener
        TextButton.TextButtonStyle transparentStyle = new TextButton.TextButtonStyle();

        // setting transparent and font
        transparentStyle.up = null;
        transparentStyle.down = null;
        transparentStyle.over = null;
        transparentStyle.font = buttonSkin.getFont("default-font"); // TODO:  implement nicer font
        TextButton playButton = new TextButton("Play", transparentStyle);
        playButton.setSize(75, 75);
        playButton.setPosition(162, 83);

        stage.addActor(playButton);

        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Clicked playButton: ");
                game.setScreen(new map1Maze(game));
                dispose();
            }

        });

        // background
        batch = new SpriteBatch(); // new creating batch image

        dungeonGroundsTexture = new Texture("SGQ_Dungeon/grounds_and_walls/grounds.png");
        menuBackground = new TextureRegion(dungeonGroundsTexture, 64, 192, 64, 64); // at (64, 192) on ground.png take a region of 64x64
        lengthxy = getBackgroundSize();
    }


    @Override
    public void render(float delta) {
        // Clear the screen to prevent previous screen from showing through
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // drawing background
        batch.begin();
        // batch.draw(background, x, y, smallest dimenstion, smallest dimenstion)
        batch.draw(menuBackground, lengthxy[1], lengthxy[2], lengthxy[0], lengthxy[0]);
        batch.end();

        // drawing stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }


    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        dungeonGroundsTexture.dispose();
    }


    // Unused implemented methods for now
    @Override public void hide() { }
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}

    public float[] getBackgroundSize() {

        // get screensize
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // calc max-length to strech without overscanning, use smallest dimension:
        float length = Math.min(screenWidth, screenHeight);
        float x = (screenWidth - length) / 2f;
        float y = (screenHeight - length) / 2f;

        // return as an array of floats
        float[] returnValue = {length, x, y};
        return returnValue;
    }


}
