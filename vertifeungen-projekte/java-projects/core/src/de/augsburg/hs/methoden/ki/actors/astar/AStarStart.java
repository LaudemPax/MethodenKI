package de.augsburg.hs.methoden.ki.actors.astar;

import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.engine.Actor;

public class AStarStart extends Actor {

    private final String SPRITE_FILE = "a-star/actor.png";

    public AStarStart(Vector2 position) {
        super(position);
        setDrawCentered(true);
        loadSpriteFromAssets(SPRITE_FILE);
    }

}
