package ru.nsu.fit.g16205.ivanishkin.model;

import ru.nsu.fit.g16205.ivanishkin.observer.Observable;
import ru.nsu.fit.g16205.ivanishkin.observer.Observer;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LifeModel implements Observable {
    private Random random = new Random();
    private List<Observer> observers = new ArrayList<>();
    private int widthM;
    private int heightN;
    private boolean saved = true;
    private static final int[][] FST_NEIGHBOURS_EVEN_OFFSETS = {{1, 0}, {-1, 0}, {-1, -1}, {-1, 1}, {0, 1}, {0, -1}};
    private static final int[][] SND_NEIGHBOURS_EVEN_OFFSETS = {{1, 1}, {1, -1}, {-2, 1}, {-2, -1}, {0, 2}, {0, -2}};
    private static final int[][] FST_NEIGHBOURS_ODD_OFFSETS = {{1, 0}, {-1, 0}, {1, -1}, {1, 1}, {0, 1}, {0, -1}};
    private static final int[][] SND_NEIGHBOURS_ODD_OFFSETS = {{-1, 1}, {-1, -1}, {2, 1}, {2, -1}, {0, 2}, {0, -2}};

    private List<List<Cell>> cells;

    public LifeModel(int width, int height) {
        widthM = width;
        heightN = height;
        cells = new ArrayList<>(heightN);
        for (int y = 0; y < height; ++y) {
            List<Cell> row = new ArrayList<>(widthM);
            cells.add(row);
            for (int x = 0; x < widthM; ++x) {
                row.add(new Cell(x, y));
            }
        }
        cells.stream()
                .flatMap(Collection::stream)
                .forEach(it -> it.initNeighbours(neighbours(it.place(), true), neighbours(it.place(), false)));
    }

    public LifeModel(Config conf) {
        this(conf.width, conf.height);
        Arrays.stream(conf.getAlive()).forEach(it -> setAlive(it, true));
    }

    public void nextStep() {
        // changed aliveness
        Set<Cell> modified = cells.stream()
                .flatMap(Collection::stream)
                .filter(Cell::nextStep)
                .collect(Collectors.toSet());

        // add all impacted neighbours
        modified.addAll(modified.stream()
                .flatMap(it -> it.getAllNeighbours().stream())
                .collect(Collectors.toSet()));

        // calculate necessary impacts
        modified.forEach(Cell::calculateImpact);
        //notifyObservers(cells.stream().flatMap(Collection::stream).toArray());
        notifyObservers(modified.toArray());
    }

    public boolean isSaved() {
        return saved;
    }

    public boolean setSaved(boolean val) {
        boolean oldVal = saved;
        saved = val;
        return oldVal;
    }

    public void random() {
        cells.stream()
                .flatMap(Collection::stream)
                .forEach(it -> setAlive(it.place(), random.nextInt(10) < 4));
        notifyObservers(cells.stream().flatMap(Collection::stream).toArray());
    }

    public void clear() {
        cells.stream()
                .flatMap(Collection::stream)
                .forEach(Cell::clear);
        notifyObservers(cells.stream().flatMap(Collection::stream).toArray());
    }

    public List<Cell> neighbours(Point p, boolean firstLvl) {
        Stream<int[]> stream;
        if (firstLvl) {
            if (p.y % 2 == 0) {
                stream = Arrays.stream(FST_NEIGHBOURS_EVEN_OFFSETS);
            } else {
                stream = Arrays.stream(FST_NEIGHBOURS_ODD_OFFSETS);
            }
        } else {
            if (p.y % 2 == 0) {
                stream = Arrays.stream(SND_NEIGHBOURS_EVEN_OFFSETS);
            } else {
                stream = Arrays.stream(SND_NEIGHBOURS_ODD_OFFSETS);
            }
        }
        return stream
                .map(it -> getCell(p.x + it[0], p.y + it[1]))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Point> getAlivePlaces() {
        return cells.stream()
                .flatMap(Collection::stream)
                .filter(Cell::isAlive)
                .map(Cell::place)
                .collect(Collectors.toList());
    }

    private Cell getCell(int x, int y) {
        try {
            return cells.get(y).get(x);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean setAlive(Point p, boolean val) {
        boolean wasAlive = getCell(p).setAlive(val);
        if (!wasAlive) {
            onUpdateAliveness(getCell(p));
        }
        return wasAlive;
    }

    public void changeAlive(Point p) {
        Cell c = getCell(p);
        c.setAlive(!c.isAlive());
        onUpdateAliveness(c);
    }

    private void onUpdateAliveness(Cell c) {
        List<Cell> updates = new ArrayList<>(13);
        c.getAllNeighbours().forEach(Cell::calculateImpact);
        updates.add(c);
        c.getAllNeighbours().stream().collect(Collectors.toCollection(() -> updates));
        notifyObservers(updates.toArray());
    }

    public Cell getCell(Point p) {
        return getCell(p.x, p.y);
    }

    public int getWidthM() {
        return widthM;
    }

    public int getHeightN() {
        return heightN;
    }

    @Override
    public void register(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        } else {
            throw new IllegalStateException("Already registered");
        }
    }

    @Override
    public void unregister(Observer o) {
        if (!observers.remove(o)) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void notifyObservers(Object[] updates) {
        saved = false;
        observers.forEach(it -> it.dispatchUpdates(this, updates));
    }
}
