package de.augsburg.hs.methoden.ki.actors.nqueens;

import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.engine.Actor;

public class QueenActor extends Actor {

    private final String SPRITE_FILE = "queens/queen.png";

    public QueenActor(Vector2 position) {
        super(position);

        loadSpriteFromAssets(SPRITE_FILE);
        setDrawCentered(true);
    }
}
