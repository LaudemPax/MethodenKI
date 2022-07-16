package de.augsburg.hs.methoden.ki.actors.astar;

import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.engine.Actor;

public class AStarAgent extends Actor {

    private final String SPRITE_FILE = "a-star/actor.png";

    public AStarAgent(Vector2 position) {
        super(position);
        setDrawCentered(true);
        loadSpriteFromAssets(SPRITE_FILE);
    }

}
