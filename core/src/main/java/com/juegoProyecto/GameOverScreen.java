package com.juegoProyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameOverScreen implements Screen {
    final CatGame game;
    OrthographicCamera camera;
    int finalScore;

    public GameOverScreen(final CatGame game, int score) {
        this.game = game;
        this.finalScore = score;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0, 0, 1); // Fondo rojizo para Game Over

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "¡GAME OVER!", 320, 300);
        game.font.draw(game.batch, "Puntuación Final: " + finalScore, 280, 240);
        game.font.draw(game.batch, "Toca para jugar de nuevo", 250, 150);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
