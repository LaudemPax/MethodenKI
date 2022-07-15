package de.augsburg.hs.methoden.ki.actors.astar;

import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.engine.AbstractActor;

public class AStarAgent extends AbstractActor {

    private final String SPRITE_FILE = "a-star/actor.png";

    public AStarAgent(Vector2 position) {
        super(position);
    }

    @Override
    protected void create() {
        loadSpriteFromAssets(SPRITE_FILE);
    }

}
