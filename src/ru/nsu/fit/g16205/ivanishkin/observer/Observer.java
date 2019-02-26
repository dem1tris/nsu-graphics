package ru.nsu.fit.g16205.ivanishkin.observer;

public interface Observer {
    void dispatchUpdates(Observable o, Object[] updated);
}
