package ru.nsu.fit.g16205.ivanishkin.model;

import java.awt.*;
import java.util.List;

public class Cell {
    public static LifeRules getRules() {
        return rules;
    }

    private static LifeRules rules = LifeRules.DEFAULT;
    private static LifeRules newRules = LifeRules.DEFAULT;

    private boolean alive = false;
    public final int x;
    public final int y;
    private double impact = 0;

    public List<Cell> getFstLvlNeighbours() {
        return fstLvlNeighbours;
    }

    public List<Cell> getSndLvlNeighbours() {
        return sndLvlNeighbours;
    }

    private List<Cell> fstLvlNeighbours;
    private List<Cell> sndLvlNeighbours;

    public Cell(final int x, final int y) {
        alive = x > 5; //todo: for debug
        this.x = x;
        this.y = y;
    }

    public Point place() {
        return new Point(x, y);
    }

    public boolean isAlive() {
        return alive;
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
     */
    public void nextStep() {
        rules = newRules;
        if (alive) {
            alive = rules.LIVE_BEGIN <= impact && impact <= rules.LIVE_END;
        } else {
            alive = rules.BIRTH_BEGIN <= impact && impact <= rules.BIRTH_END;
        }
        impact = 0;
    }

    /**
     * Changes rules since the nearest step.
     *
     * @param val - rules that will be applied;
     */
    public static void changeRules(final LifeRules val) {
        newRules = val;
    }

    public double getImpact() {
        return impact;
    }
}
