package com.juegoProyecto;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;

public class MainMenuScreen implements Screen {
    final CatGame game;
    OrthographicCamera camera;
    Texture background;
    private GlyphLayout layout = new GlyphLayout();

    public MainMenuScreen(final CatGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Imagen de fondo (asegúrate de que esté en la carpeta assets)
        background = new Texture("menu_bg.png");

        // Escalamos la fuente estándar para que se vea grande y nítida
        game.font.getData().setScale(2.5f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // --- MEJORA DE RESOLUCIÓN ---
        // Esto suaviza los píxeles de la fuente para que no se vea "sucio"
        game.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);

        // --- TÍTULO: GATO GLOTON (GRANDE Y CENTRADO) ---
        game.font.getData().setScale(4f); // Tamaño grande
        String titulo = "GATO GLOTON";
        layout.setText(game.font, titulo);
        float xTitulo = (800 - layout.width) / 2; // Centrado exacto

        // Sombra para que resalte
        game.font.setColor(new Color(0, 0, 0, 0.6f));
        game.font.draw(game.batch, titulo, xTitulo + 3, 403);
        // Texto principal
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, titulo, xTitulo, 405);

        // --- INSTRUCCIONES (MEDIANAS Y CENTRADAS) ---
        game.font.getData().setScale(1.5f);
        String instrucciones = "Atrapa peces y evita el veneno";
        layout.setText(game.font, instrucciones);
        float xInst = (800 - layout.width) / 2;

        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, instrucciones, xInst + 1, 251);
        game.font.setColor(Color.YELLOW);
        game.font.draw(game.batch, instrucciones, xInst, 253);

        // --- BOTÓN: TOCA PARA EMPEZAR (CENTRADO) ---
        game.font.getData().setScale(2.5f);
        String empezar = "TOCA PARA EMPEZAR";
        layout.setText(game.font, empezar);
        float xEmpezar = (800 - layout.width) / 2;

        // Sombra
        game.font.setColor(new Color(0, 0, 0, 0.5f));
        game.font.draw(game.batch, empezar, xEmpezar + 2, 102);
        // Texto principal
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, empezar, xEmpezar, 104);

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

    @Override
    public void dispose() {
        background.dispose();
    }
}
