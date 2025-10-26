package com.Team10.Team10Maze;

// Drawing, screen, and texture imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20; // controls openGL drawing
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.Stage;

// input handling
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.Input;

// Application and Window class imports
import com.badlogic.gdx.utils.ScreenUtils; // useful library for screen tasks eg clearing screen

// Algorithm-based/ datatype imports
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.StringBuilder;

// TODO: PRIORITY -- convert to tiled

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class map1Maze implements Screen {
    private final mazeGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;

    private TiledMap tiledMap1;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera cameraMap1;

    private float timeLeft = 3f;

    public map1Maze(mazeGame game) {
        this.game = game;
        addTime(60f);
    }

    @Override public void show() {
        // load map1.tmx
        tiledMap1 = new TmxMapLoader().load("maps/Team10Maze1.tmx");


        // Renderer, 1 tile = 64 pixels
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap1, 1f / 64f);

        cameraMap1 = new OrthographicCamera();
        cameraMap1.setToOrtho(false, 18, 18); // y-axis increasing towards top of screen, shows 18x18
    }


    @Override public void dispose() {
        font.dispose();
        batch.dispose();
    }

    @Override
    public void render(float delta) {
        cameraMap1.update();

        mapRenderer.setView(cameraMap1);
        mapRenderer.render();

    }

    public void addTime(float extraTime) {
        System.out.println("adding " + extraTime + " seconds");
        timeLeft += extraTime;
    }

    public void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            System.out.println("W");
            moveCharacter(0, -1); // moved up
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            System.out.println("A");
            moveCharacter(-1, 0); // moved left
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            System.out.println("S");
            moveCharacter(0, 1); // moved down
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            System.out.println("D");
            moveCharacter(1, 0); // moved right
        }

    }

    public void moveCharacter(int xDifference, int yDifference){
        System.out.println("moving nowhting");
    }

    public void goalReached() {

        System.out.println("CONGRATULTATIOANS");
        game.setScreen(new mainMenu(game));
        dispose();

    }

    @Override public void hide() { }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
}
