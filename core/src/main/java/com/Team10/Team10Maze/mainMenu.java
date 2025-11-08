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

        viewport = new FitViewport(640, 480); 
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        // implementing skin and stage
        stage = new Stage(new FitViewport(640, 480));
        Gdx.input.setInputProcessor(stage);
        Skin buttonSkin = new Skin(Gdx.files.internal("uiskin.json"));


        // implementing button and click listener
        TextButton.TextButtonStyle transparentStyle = new TextButton.TextButtonStyle();
        transparentStyle.font = buttonSkin.getFont("default-font");

        playButton = new TextButton("Play", transparentStyle);
        playButton.setSize(120, 60);
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
        menuBackground = new TextureRegion(dungeonGroundsTexture, 64, 192, 64, 64);

        updateLayout(); // Position button based on current screen size
    }

    private void updateLayout() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // Resizes the play button whenever width/height changes
        playButton.setPosition(
                worldWidth / 4f - playButton.getWidth() / 2f,
                worldHeight / 4f - playButton.getHeight() / 2f
        );
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(menuBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        updateLayout();   // Re-center UI + background on resize
    }

    @Override public void dispose() {
        batch.dispose();
        stage.dispose();
        dungeonGroundsTexture.dispose();
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
