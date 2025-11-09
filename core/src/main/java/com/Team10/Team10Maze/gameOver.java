package com.Team10.Team10Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

public class gameOver implements Screen {

    private final mazeGame game;
    private boolean gameWon;
    private Stage stage;
    private Viewport viewport;

    /**
     * Called when a game is determined as over
     * 
     * @param game The game instance that allows us to change the screen
     * @param gameWon True if the player won the game, false if they lost
     */
    public gameOver(mazeGame game, boolean gameWon) {
        this.game = game;
        this.gameWon = gameWon;
    }


    /**
     * Creates the menu the user will see.
     * <p>
     * This function is responsible for:
     * <ul>
     *  <li>Creating text and updating all relevant data for them</li>
     *  <li>Creates a key handler to move player to main menu</li>
     * </ul>
     */
    @Override
    public void show() {
        viewport = new FitViewport(640, 480);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.WHITE;

        Label endText = new Label(this.gameWon ? "YOU WIN!" : "YOU LOST!", labelStyle);
        endText.setFontScale(2f);
        endText.setSize(endText.getPrefWidth(), endText.getPrefHeight());
        endText.setAlignment(Align.center);
        endText.setPosition(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, Align.center);
        stage.addActor(endText);


        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Label pressKey = new Label("Press any key to continue", labelStyle);
                pressKey.setFontScale(1.2f);
                pressKey.setSize(pressKey.getPrefWidth(), pressKey.getPrefHeight());
                pressKey.setAlignment(Align.center);
                pressKey.setPosition(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f - 80f, Align.center);
                pressKey.getColor().a = 0f;    

                stage.addActor(pressKey);
                pressKey.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1f), Actions.fadeOut(1f))));

                Gdx.input.setInputProcessor(stage);
                stage.addListener(new ClickListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        goToMainMenu();
                        return true;
                    }

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        goToMainMenu();
                    }
                });
            }
        }, 3);
    }

    /**
     * Returns the player back to the main menu once a key has been pressed in this screen
     */
    private void goToMainMenu() {
        System.out.println("Game Over screen closed - moving to next screen.");
        game.setScreen(new mainMenu(game));
        dispose();
    }

    /** 
     * @param delta Internal timer of LibGDX to ensure everything is running smooth irregardless of FPS
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /** 
     * @param width New width of the user's window
     * @param height New height of the user's window
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Called to dispose of all text and key handlers used in this screen
     */
    @Override public void dispose() {
        stage.dispose();
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
