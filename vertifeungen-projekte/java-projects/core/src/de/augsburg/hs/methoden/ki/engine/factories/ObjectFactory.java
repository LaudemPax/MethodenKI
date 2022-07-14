package de.augsburg.hs.methoden.ki.engine.factories;

import de.augsburg.hs.methoden.ki.engine.objects.TitleScreenLevelObject;

public class ObjectFactory {

    private static ObjectFactory singletonInstance;

    private ObjectFactory() {
    }

    public static ObjectFactory getInstance() {
        if(singletonInstance == null) {
            singletonInstance = new ObjectFactory();
        }

        return singletonInstance;
    }


}
