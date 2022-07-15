package de.augsburg.hs.methoden.ki.actors.astar;

import com.badlogic.gdx.math.Vector2;
import de.augsburg.hs.methoden.ki.engine.AbstractActor;

public class AStarTarget extends AbstractActor {

    private final String SPRITE_FILE = "a-star/target.png";

    public AStarTarget(Vector2 position) {
        super(position);

        setDrawCentered(true);
    }
    @Override
    protected void create() {
        loadSpriteFromAssets(SPRITE_FILE);
    }
}
