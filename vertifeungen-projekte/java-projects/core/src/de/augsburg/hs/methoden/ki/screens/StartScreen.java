package de.augsburg.hs.methoden.ki.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.augsburg.hs.methoden.ki.MainGame;
import de.augsburg.hs.methoden.ki.engine.GameScreen;

import java.util.ArrayList;

public class StartScreen extends GameScreen {

    private ArrayList<GameScreen> levels;

    public StartScreen(MainGame game) {
        super(game);

        levels = new ArrayList<>();
    }

    @Override
    public void create() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    protected void draw(SpriteBatch batch) {

    }
}
