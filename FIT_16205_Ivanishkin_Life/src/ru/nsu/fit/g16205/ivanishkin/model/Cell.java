package ru.nsu.fit.g16205.ivanishkin.model;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cell {
    public static LifeRules getRules() {
        return rules;
    }

    private static LifeRules rules = LifeRules.DEFAULT;

    private boolean alive = false;
    public final int x;
    public final int y;
    private double impact = 0;
    private List<Cell> fstLvlNeighbours;
    private List<Cell> sndLvlNeighbours;


    public List<Cell> getFstLvlNeighbours() {
        return fstLvlNeighbours;
    }

    public List<Cell> getSndLvlNeighbours() {
        return sndLvlNeighbours;
    }

    public List<Cell> getAllNeighbours() {
        return Stream
                .concat(fstLvlNeighbours.stream(), sndLvlNeighbours.stream())
                .collect(Collectors.toList());
    }

    public Cell(final int x, final int y, boolean alive) {
        this(x, y);
        this.alive = alive;
    }

    public Cell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Point place() {
        return new Point(x, y);
    }

    public boolean isAlive() {
        return alive;
    }

    public void clear() {
        alive = false;
        impact = 0;
    }

    public void initNeighbours(final List<Cell> fstLvl, final List<Cell> sndLvl) {
        fstLvlNeighbours = fstLvl;
        sndLvlNeighbours = sndLvl;
    }

    public boolean setAlive(boolean val) {
        boolean oldVal = alive;
        alive = val;
        return oldVal;
    }

    /**
     * Calculate an impact from cell's neighbours
     */
    public void calculateImpact() {
        impact = 0;
        for (Cell c : fstLvlNeighbours) {
            if (c.isAlive()) {
                impact += rules.FST_IMPACT;
            }
        }
        for (Cell c : sndLvlNeighbours) {
            if (c.isAlive()) {
                impact += rules.SND_IMPACT;
            }
        }
    }

    /**
     * Aliveness changed based on current impact. Impact sets to zero;
     *
     * @return `true` if its aliveness was modified, `false` otherwise
     */
    public boolean nextStep() {
        boolean wasAlive = alive;
        if (alive) {
            alive = rules.LIFE_BEGIN <= impact && impact <= rules.LIFE_END;
        } else {
            alive = rules.BIRTH_BEGIN <= impact && impact <= rules.BIRTH_END;
        }
        impact = 0;
        return wasAlive != alive;
    }

    /**
     * Changes rules since the nearest step.
     *
     * @param val - rules that will be applied;
     */
    public static void changeRules(final LifeRules val) {
        rules = val;
    }

    public double getImpact() {
        return impact;
    }
}
