package com.juegoProyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final CatGame game;
    OrthographicCamera camera;

    Texture bgTex, catTex, fishTex, canTex, poisonTex;
    Sound eatSound, powerupSound, hurtSound;
    Music music;

    Rectangle cat;
    Vector3 touchPos;

    // Clase interna para manejar diferentes tipos de comida
    class FoodDrop {
        Rectangle bounds;
        int type; // 0=Pez, 1=Lata, 2=Veneno
    }

    Array<FoodDrop> foods;
    long lastDropTime;

    // Variables de estado del juego
    int score = 0;
    int lives = 3;
    float fallSpeed = 200f; // Velocidad inicial

    public GameScreen(final CatGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Cargar recursos [cite: 17]
        bgTex = new Texture("casa_bg.png");
        catTex = new Texture("gato.png");
        fishTex = new Texture("pez.png");
        canTex = new Texture("lata.png");
        poisonTex = new Texture("veneno.png");

        eatSound = Gdx.audio.newSound(Gdx.files.internal("comer.wav"));
        powerupSound = Gdx.audio.newSound(Gdx.files.internal("powerup.wav"));
        hurtSound = Gdx.audio.newSound(Gdx.files.internal("mal.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("musica.wav"));

        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        cat = new Rectangle();
        cat.x = 800 / 2 - 64 / 2; // Centro de la pantalla
        cat.y = 20;
        cat.width = 64;
        cat.height = 64;

        touchPos = new Vector3();
        foods = new Array<>();
        spawnFood();
    }

    private void spawnFood() {
        FoodDrop drop = new FoodDrop();
        drop.bounds = new Rectangle();
        drop.bounds.x = MathUtils.random(0, 800 - 64);
        drop.bounds.y = 480;
        drop.bounds.width = 64;
        drop.bounds.height = 64;

        // Lógica avanzada: % de aparición
        int rand = MathUtils.random(1, 100);
        if (rand <= 60) drop.type = 0;      // 60% Pez
        else if (rand <= 80) drop.type = 1; // 20% Lata
        else drop.type = 2;                 // 20% Veneno

        foods.add(drop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // --- DIBUJAR ---
        game.batch.begin();
        game.batch.draw(bgTex, 0, 0, 800, 480);

        // HUD (Puntuación y vidas)
        game.font.draw(game.batch, "Puntos: " + score, 20, 460);
        game.font.draw(game.batch, "Vidas: " + lives, 650, 460);

        game.batch.draw(catTex, cat.x, cat.y, cat.width, cat.height);

        for (FoodDrop f : foods) {
            if (f.type == 0) game.batch.draw(fishTex, f.bounds.x, f.bounds.y);
            else if (f.type == 1) game.batch.draw(canTex, f.bounds.x, f.bounds.y);
            else game.batch.draw(poisonTex, f.bounds.x, f.bounds.y);
        }
        game.batch.end();

        // --- INPUT ---
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            cat.x = touchPos.x - 64 / 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) cat.x -= 400 * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) cat.x += 400 * delta;

        // Limites del gato
        if (cat.x < 0) cat.x = 0;
        if (cat.x > 800 - 64) cat.x = 800 - 64;

        // --- LÓGICA ---
        // Generar comida cada segundo
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnFood();

        for (int i = foods.size - 1; i >= 0; i--) {
            FoodDrop f = foods.get(i);
            f.bounds.y -= fallSpeed * delta; // Uso de DeltaTime

            if (f.bounds.y + 64 < 0) {
                foods.removeIndex(i);
            } else if (f.bounds.overlaps(cat)) { // Colisiones [cite: 16]
                if (f.type == 0) {
                    score += 10;
                    eatSound.play();
                } else if (f.type == 1) {
                    fallSpeed += 50; // Aumenta la dificultad
                    powerupSound.play();
                } else if (f.type == 2) {
                    lives--;
                    hurtSound.play();
                }
                foods.removeIndex(i);
            }
        }

        // Condición de derrota
        if (lives <= 0) {
            game.setScreen(new GameOverScreen(game, score));
            dispose();
        }
    }

    @Override
    public void dispose() {
        bgTex.dispose(); catTex.dispose(); fishTex.dispose();
        canTex.dispose(); poisonTex.dispose();
        eatSound.dispose(); powerupSound.dispose(); hurtSound.dispose();
        music.dispose();
    }

    // Métodos vacíos obligatorios de Screen
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
