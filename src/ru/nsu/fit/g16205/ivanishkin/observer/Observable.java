package ru.nsu.fit.g16205.ivanishkin.observer;

import java.util.List;

public interface Observable {
    void register(Observer o);
    void unregister(Observer o);
    void notifyObservers(Object[] updates);
}
