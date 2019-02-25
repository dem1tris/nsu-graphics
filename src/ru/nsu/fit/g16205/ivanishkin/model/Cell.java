package ru.nsu.fit.g16205.ivanishkin.model;

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
    private List<Cell> fstLvlNeighbours;
    private List<Cell> sndLvlNeighbours;

    public Cell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * Should be called by the neighbour to make an impact on this cell.
     *
     * @param level - neighbour's level.
     */
    public void updateImpact(final int level) {
        switch (level) {
            case 1:
                impact += rules.FST_IMPACT;
            case 2:
                impact += rules.SND_IMPACT;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void initNeighbours(final List<Cell> fstLvl, final List<Cell> sndLvl) {
        fstLvlNeighbours = fstLvl;
        sndLvlNeighbours = sndLvl;
    }

    /**
     * Make an impact on cell's neighbours
     */
    public void broadcastImpact() {
        if (alive) {
            for (Cell cell : fstLvlNeighbours) {
                cell.updateImpact(1);
            }
            for (Cell cell : sndLvlNeighbours) {
                cell.updateImpact(2);
            }
        }
    }

    /**
     * Aliveness changed based on current impact. Impact sets to zero;
     */
    public void nextStep() {
        if (alive) {
            alive = rules.LIVE_BEGIN <= impact && impact <= rules.LIVE_END;
        } else {
            alive = rules.BIRTH_BEGIN <= impact && impact <= rules.BIRTH_END;
        }
        impact = 0;
        rules = newRules;
    }

    /**
     * Changes rules since the next step.
     *
     * @param val - rules that will be applied;
     */
    public static void changeRules(final LifeRules val) {
        newRules = val;
    }
}
