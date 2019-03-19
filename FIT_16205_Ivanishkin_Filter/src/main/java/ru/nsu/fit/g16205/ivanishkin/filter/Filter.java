package ru.nsu.fit.g16205.ivanishkin.filter;

import java.awt.image.BufferedImage;

public interface Filter {
    BufferedImage apply(BufferedImage target);
}
