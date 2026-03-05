package com.juegoProyecto;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CatGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font; // Usaremos la fuente por defecto para el texto

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        // Hacemos la fuente un poco más grande
        font.getData().setScale(2f);
        // Iniciamos en el Menú
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // ¡Muy importante para que renderice las pantallas!
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
