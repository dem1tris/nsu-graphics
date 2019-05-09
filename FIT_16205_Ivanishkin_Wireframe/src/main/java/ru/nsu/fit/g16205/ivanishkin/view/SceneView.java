package ru.nsu.fit.g16205.ivanishkin.view;

import ru.nsu.fit.g16205.ivanishkin.geom.PointTranslator;
import ru.nsu.fit.g16205.ivanishkin.geom.PointTranslatorBuilder;
import ru.nsu.fit.g16205.ivanishkin.geom.UniformPoint3D;
import ru.nsu.fit.g16205.ivanishkin.model.Figure3D;
import ru.nsu.fit.g16205.ivanishkin.model.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

import static ru.nsu.fit.g16205.ivanishkin.utils.Utils.clearImg;
import static ru.nsu.fit.g16205.ivanishkin.utils.Utils.drawSegmented;

public class SceneView extends JPanel {
    private UniformPoint3D pCam = new UniformPoint3D(-10, 0, 0, 1);
    private UniformPoint3D pView = new UniformPoint3D(10, 0, 0, 1);
    private UniformPoint3D vUp = new UniformPoint3D(0, 1, 0, 1);
    private double zn = 0.7;
    private double zf = 0.5;
    private double sw = 0.5;
    private double sh = 0.5;

    private PointTranslator toCam = new PointTranslatorBuilder()
            .rotateOverY(Math.PI / 2)
            .shift(-pCam.getX(), -pCam.getY(), -pCam.getZ()).build();

    private PointTranslator pspProjection = new PointTranslatorBuilder()
            .pspProject(zf).build();

    private Spline spline;
    private Figure3D figure;
    private BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);

    public void setSpline(Spline spline) {
        this.spline = spline;
        this.figure = new Figure3D(spline, new UniformPoint3D(250, 250, 100, 0));

        this.figure.translate(new PointTranslatorBuilder()
                .rotateOverX(Math.PI / 3)
//                .rotateOverY(Math.PI / 3)
                .rotateOverZ(Math.PI / 3)
                .build());
    }

    private void drawFigure(Figure3D figure) {
        Graphics2D g = img.createGraphics();
        // todo: from spline
        g.setColor(Color.BLACK);
        for (List<UniformPoint3D> oneSpline : figure.getSplinesPoints()) {
            List<Point2D> points = oneSpline.stream()
                    .map(toCam::translate)
                    .map(p -> new Point2D.Double(p.getX(), p.getY()))
                    .collect(Collectors.toList());
            drawSegmented(g, points, null);
        }

        for (List<UniformPoint3D> oneParallel : figure.getParallelsPoints()) {
            List<Point2D> points = oneParallel.stream()
                    .map(toCam::translate)
                    .map(p -> new Point2D.Double(p.getX(), p.getY()))
                    .collect(Collectors.toList());
            drawSegmented(g, points, null);
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        clearImg(img, Color.WHITE);

        drawFigure(figure);

        g.drawImage(img, 0, 0, this);
    }
}
