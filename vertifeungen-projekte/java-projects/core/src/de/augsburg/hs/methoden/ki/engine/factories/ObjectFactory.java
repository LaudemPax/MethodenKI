package de.augsburg.hs.methoden.ki.engine.factories;

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
