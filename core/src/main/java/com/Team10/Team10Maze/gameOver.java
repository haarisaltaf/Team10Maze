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

    public gameOver(mazeGame game, boolean gameWon) {
        this.game = game;
        this.gameWon = gameWon;
    }

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

    private void goToMainMenu() {
        System.out.println("Game Over screen closed - moving to next screen.");
        game.setScreen(new mainMenu(game));
        dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void dispose() {
        stage.dispose();
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
