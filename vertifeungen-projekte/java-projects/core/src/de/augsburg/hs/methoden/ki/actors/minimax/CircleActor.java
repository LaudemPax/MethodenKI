package de.augsburg.hs.methoden.ki.actors.minimax;

import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.engine.Actor;

public class CircleActor extends Actor {

    private final String SPRITE_FILE = "minimax/circle.png";

    public CircleActor(Vector2 position) {
        super(position);
        loadSpriteFromAssets(SPRITE_FILE);
        setDrawCentered(true);
    }

}
