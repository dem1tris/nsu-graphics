package ru.nsu.fit.g16205.ivanishkin.model;

public class LifeModel {
    private int widthM;
    private int heightN;
    private int cellSizeK;

    public LifeModel(int width, int height) {
        widthM = width;
        heightN = height;
    }

    public int getWidthM() {
        return widthM;
    }

    public void setWidthM(int widthM) {
        this.widthM = widthM;
    }

    public int getHeightN() {
        return heightN;
    }

    public void setHeightN(int heightN) {
        this.heightN = heightN;
    }
}
